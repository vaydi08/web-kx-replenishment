SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[info_tessera]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[info_tessera](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[pid] [int] NOT NULL,
	[name] [nvarchar](20) NOT NULL,
	[weight] [float] NULL,
	[clarity] [nvarchar](20) NULL,
	[color] [nvarchar](20) NULL,
	[cut] [nvarchar](20) NULL,
 CONSTRAINT [PK_info_tessera] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO

IF NOT EXISTS (SELECT * FROM dbo.sysindexes WHERE id = OBJECT_ID(N'[dbo].[info_tessera]') AND name = N'IX_info_tessera_pid')
CREATE NONCLUSTERED INDEX [IX_info_tessera_pid] ON [dbo].[info_tessera] 
(
	[pid] ASC
) ON [PRIMARY]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[info_manufactory]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[info_manufactory](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[pid] [int] NOT NULL,
	[name] [nvarchar](20) NOT NULL,
	[mf_code] [nvarchar](20) NULL,
	[remark] [nvarchar](20) NULL,
 CONSTRAINT [PK_info_manufactory] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO

IF NOT EXISTS (SELECT * FROM dbo.sysindexes WHERE id = OBJECT_ID(N'[dbo].[info_manufactory]') AND name = N'IX_info_manufactory_pid')
CREATE NONCLUSTERED INDEX [IX_info_manufactory_pid] ON [dbo].[info_manufactory] 
(
	[pid] ASC
) ON [PRIMARY]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[info_category]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[info_category](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[ccode] [varchar](20) NOT NULL,
	[cname] [nvarchar](20) NOT NULL,
	[clevel] [int] NOT NULL,
	[parent] [int] NOT NULL CONSTRAINT [DF_category_parent]  DEFAULT ((0)),
	[lastUpdateTime] [datetime] NOT NULL CONSTRAINT [DF_category_lastUpdateTime]  DEFAULT (getdate()),
 CONSTRAINT [PK_category] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[stock_real_package]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[stock_real_package](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[shopid] [int] NOT NULL,
	[name] [nvarchar](20) NOT NULL,
	[inserttime] [datetime] NOT NULL CONSTRAINT [DF_stock_real_package_inserttime]  DEFAULT (getdate()),
 CONSTRAINT [PK_stock_real_package] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[stock_real]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[stock_real](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[pkgid] [int] NOT NULL,
	[pid] [int] NOT NULL,
	[weight] [float] NOT NULL,
	[stock] [int] NOT NULL,
	[sale] [int] NOT NULL,
 CONSTRAINT [PK_stock_real] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[stock_check]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[stock_check](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[pid] [int] NOT NULL,
	[shopid] [int] NOT NULL,
	[minweight] [float] NOT NULL CONSTRAINT [DF_check_stock_minweight]  DEFAULT ((0)),
	[maxweight] [float] NOT NULL CONSTRAINT [DF_check_stock_maxweight]  DEFAULT ((0)),
	[stock] [int] NOT NULL,
	[stocktype] [smallint] NOT NULL CONSTRAINT [DF_check_stock_stocktype]  DEFAULT ((1)),
	[lastUpdateTime] [datetime] NOT NULL CONSTRAINT [DF_check_stock_lastUpdateTime]  DEFAULT (getdate()),
 CONSTRAINT [PK_stock_check] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[info_product]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[info_product](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[type1] [int] NOT NULL,
	[type2] [int] NOT NULL,
	[type3] [int] NOT NULL,
	[type4] [int] NOT NULL,
	[pname] [nvarchar](20) NOT NULL,
	[pcode] [varchar](20) NOT NULL,
	[unit] [nvarchar](10) NOT NULL CONSTRAINT [DF_info_product_unit]  DEFAULT (N'克'),
	[lastUpdateTime] [datetime] NOT NULL CONSTRAINT [DF_product_lastUpdateTime]  DEFAULT (getdate()),
 CONSTRAINT [PK_info_product] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[compare]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[compare](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[importid] [int] NOT NULL,
	[shopid] [int] NOT NULL,
	[shopname] [varchar](20) NOT NULL,
	[pid] [int] NOT NULL,
	[pname] [varchar](20) NOT NULL,
	[pcode] [varchar](20) NOT NULL,
	[minweight] [float] NOT NULL,
	[maxweight] [float] NOT NULL,
	[checkstock] [int] NOT NULL,
	[weight] [float] NOT NULL,
	[stock] [int] NOT NULL,
	[lastUpdateTime] [datetime] NOT NULL CONSTRAINT [DF_compare_lastUpdateTime]  DEFAULT (getdate()),
 CONSTRAINT [PK_compare] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[compare1]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[compare1](
	[shopname] [nvarchar](20) NOT NULL,
	[pname] [nvarchar](20) NOT NULL,
	[pcode] [varchar](20) NOT NULL,
	[unit] [nvarchar](10) NOT NULL,
	[pkgid] [int] NULL,
	[pid] [int] NOT NULL,
	[shopid] [int] NOT NULL,
	[minweight] [float] NOT NULL,
	[maxweight] [float] NOT NULL,
	[checkstock] [int] NOT NULL,
	[weight] [float] NULL,
	[stock] [int] NOT NULL
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[proc_compare]') AND OBJECTPROPERTY(id,N'IsProcedure') = 1)
BEGIN
EXEC dbo.sp_executesql @statement = N'
CREATE PROCEDURE [dbo].[proc_compare]
	@compname varchar(20),
	@pkgid int
AS
BEGIN
	SET NOCOUNT ON;

	declare @tablename varchar(100);
	set @tablename = ''compare'' + @compname

	if object_id(@tablename,N''U'') is not null
	begin
		exec(''drop table '' + @tablename)
	end

	declare @sql varchar(1000);
	
	set @sql = ''select * into '' + @tablename + '' from
	(select shop.name as shopname,p.name as pname,p.pcode,p.unit,stock.* from 
		(select kc.pkgid,hd.pid,hd.shopid,hd.minweight,hd.maxweight,hd.stock as checkstock,kc.weight,isnull(kc.stock,0) as stock
		from stock_check hd inner join 
		(select p.id as pkgid,p.shopid,r.id,r.pid,r.weight,r.stock from stock_real_package p left join stock_real r on p.id=r.pkgid and p.id='' + convert(varchar(10),@pkgid) + '') kc 
		on hd.pid=kc.pid 
		and hd.shopid=kc.shopid 
		and (kc.weight >= hd.minweight and kc.weight <hd.maxweight)) stock
	inner join info_shop shop on stock.shopid=shop.id
	inner join info_product p on stock.pid=p.id
	) supply''

	--print @sql
	exec(@sql)
	
	SET NOCOUNT OFF;

	exec(''select * from '' + @tablename)
END
' 
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[compare2]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[compare2](
	[shopname] [nvarchar](20) NOT NULL,
	[pname] [nvarchar](20) NOT NULL,
	[pcode] [varchar](20) NOT NULL,
	[unit] [nvarchar](10) NOT NULL,
	[pkgid] [int] NOT NULL,
	[pid] [int] NOT NULL,
	[shopid] [int] NOT NULL,
	[minweight] [float] NOT NULL,
	[maxweight] [float] NOT NULL,
	[checkstock] [int] NOT NULL,
	[weight] [float] NULL,
	[stock] [int] NOT NULL
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[info_shop]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[info_shop](
	[id] [int] IDENTITY(1,1) NOT NULL,
	[scode] [varchar](20) NOT NULL,
	[name] [nvarchar](20) NOT NULL,
	[address] [nvarchar](50) NULL,
	[tel] [varchar](20) NULL,
	[shorttel] [varchar](20) NULL,
	[remark] [varchar](50) NULL,
	[lastUpdateTime] [datetime] NOT NULL CONSTRAINT [DF_info_shop_lastUpdateTime]  DEFAULT (getdate()),
 CONSTRAINT [PK_info_shop] PRIMARY KEY CLUSTERED 
(
	[id] ASC
) ON [PRIMARY]
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[proc_DataPagination]') AND OBJECTPROPERTY(id,N'IsProcedure') = 1)
BEGIN
EXEC dbo.sp_executesql @statement = N'CREATE PROCEDURE [dbo].[proc_DataPagination]  
(  
@tblName nvarchar(200),        ----要显示的表或多个表的连接 
@fldName nvarchar(500) = ''*'', ----要显示的字段列表 
@pageSize int = 10,        ----每页显示的记录个数 
@page        int = 1,        ----要显示那一页的记录 
@fldSort nvarchar(200) = null, ----排序字段列表或条件 
@Sort        bit = 0,        ----排序方法，为升序，为降序(如果是多字段排列Sort指代最后一个排序字段的排列顺序(最后一个排序字段不加排序标记)--程序传参如：'' SortA Asc,SortB Desc,SortC '')  
@strCondition nvarchar(1000) = null, ----查询条件,不需where  
@ID        nvarchar(150),        ----主表的主键 
@Dist    bit = 0,       ----是否添加查询字段的DISTINCT 默认不添加/1添加 
@pageCount int = 1 output,          ----查询结果分页后的总页数 
@Counts int = 1 output             ----查询到的记录数 
)  
AS  
SET NOCOUNT ON  
Declare @sqlTmp nvarchar(1000)        ----存放动态生成的SQL语句 
Declare @strTmp nvarchar(1000)        ----存放取得查询结果总数的查询语句 
Declare @strID nvarchar(1000)        ----存放取得查询开头或结尾ID的查询语句 
  
Declare @strSortType nvarchar(10) ----数据排序规则A  
Declare @strFSortType nvarchar(10) ----数据排序规则B  
  
Declare @SqlSelect nvarchar(50)        ----对含有DISTINCT的查询进行SQL构造 
Declare @SqlCounts nvarchar(50)       ----对含有DISTINCT的总数查询进行SQL构造 
  
  
if @Dist   = 0  
begin  
set @SqlSelect = ''select ''  
set @SqlCounts = ''Count(0)''  
end  
else  
begin  
set @SqlSelect = ''select distinct ''  
set @SqlCounts = ''Count(DISTINCT ''+@ID+'')''  
end  
   
if @Sort=0  
begin  
set @strFSortType='' DESC ''  
set @strSortType='' DESC ''  
end  
else  
begin  
set @strFSortType='' ASC ''  
set @strSortType='' ASC ''  
end  
  
if(@fldSort is not null and @fldSort <>'''') 
begin 
set @fldSort='',''+@fldSort 
end 
else 
begin 
set @fldSort='' '' 
end 
  
--------生成查询语句--------  
--此处@strTmp为取得查询结果数量的语句 
if @strCondition is null or @strCondition='''' --没有设置显示条件 
begin  
set @sqlTmp =   @fldName + '' From '' + @tblName  
set @strTmp = @SqlSelect+'' @Counts=''+@SqlCounts+'' FROM ''+@tblName  
set @strID = '' From '' + @tblName  
end  
else  
begin  
set @sqlTmp = + @fldName + ''From '' + @tblName + '' where (1>0) '' + @strCondition  
set @strTmp = @SqlSelect+'' @Counts=''+@SqlCounts+'' FROM ''+@tblName + '' where (1>0) '' + @strCondition  
set @strID = '' From '' + @tblName + '' where (1>0) '' + @strCondition  
end  
  
----取得查询结果总数量-----  
exec sp_executesql @strTmp,N''@Counts int out '',@Counts out  
declare @tmpCounts int  

if @Counts = 0  
set @tmpCounts = 1  
else  
set @tmpCounts = @Counts  
  
--取得分页总数 
set @pageCount=(@tmpCounts+@pageSize-1)/@pageSize  
  
/**//**当前页大于总页数取最后一页**/  
if @page>@pageCount  
       set @page=@pageCount  
  
--/*-----数据分页分处理-------*/  
declare @pageIndex int --总数/页大小 
declare @lastcount int --总数%页大小  
  
set @pageIndex = @tmpCounts/@pageSize  
set @lastcount = @tmpCounts%@pageSize  
if @lastcount > 0  
       set @pageIndex = @pageIndex + 1  
else  
       set @lastcount = @pagesize  
  
--为配合显示 
--set nocount off  
--select @page curpage,@pageSize pagesize,@pageCount countpage,@tmpCounts [Rowcount]  
--set nocount on  
  
   --//***显示分页 
if @strCondition is null or @strCondition='''' --没有设置显示条件 
begin  
       if @pageIndex <2 or @page <=@pageIndex / 2 + @pageIndex % 2   --前半部分数据处理 
         begin  
            if @page=1  
                   set @strTmp=@SqlSelect+'' top ''+ CAST(@pageSize as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName                         
                     +'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort 
            else  
            begin                   
                   set @strTmp=@SqlSelect+'' top ''+ CAST(@pageSize as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName  
                     +'' where ''+@ID  
                   if @Sort=0  
                     set @strTmp = @strTmp + ''>(select max(''  
                   else  
                     set @strTmp = @strTmp + '' <(select min(''  
                   set @strTmp = @strTmp + @ID +'') from (''+ @SqlSelect+'' top ''+ CAST(@pageSize*(@page-1) as Varchar(20)) +'' ''+ @ID +'' from ''+@tblName  
                     +'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort+'') AS TBMinID)''  
                     +'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort 
            end    
         end  
       else  
            
         begin  
         set @page = @pageIndex-@page+1 --后半部分数据处理 
            if @page <= 1 --最后一页数据显示          
                   set @strTmp=@SqlSelect+'' * from (''+@SqlSelect+'' top ''+ CAST(@lastcount as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName  
                     +'' order by ''+ @ID +'' ''+ @strSortType+@fldSort+'') AS TempTB''+'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort 
            else  
                   begin  
                   set @strTmp=@SqlSelect+'' * from (''+@SqlSelect+'' top ''+ CAST(@pageSize as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName  
                     +'' where ''+@ID  
                     if @Sort=0  
                        set @strTmp=@strTmp+'' <(select min(''  
                     else  
                        set @strTmp=@strTmp+'' >(select max(''  
   set @strTmp=@strTmp+ @ID +'') from(''+ @SqlSelect+'' top ''+ CAST(@pageSize*(@page-2)+@lastcount as Varchar(20)) +'' ''+ @ID +'' from ''+@tblName  
                     +'' order by ''+ @ID +'' ''+ @strSortType+@fldSort+'') AS TBMaxID)''  
                     +'' order by ''+ @ID +'' ''+ @strSortType+@fldSort+'') AS TempTB''+'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort 
                   end  
         end  
  
end  
  
else --有查询条件 
begin  
       if @pageIndex <2 or @page <=@pageIndex / 2 + @pageIndex % 2   --前半部分数据处理 
       begin  
            if @page=1  
                   set @strTmp=@SqlSelect+'' top ''+ CAST(@pageSize as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName                         
                     +'' where 1=1 '' + @strCondition + '' order by ''+ @ID+'' ''+ @strFSortType+@fldSort 
            else  
            begin                   
                   set @strTmp=@SqlSelect+'' top ''+ CAST(@pageSize as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName  
                     +'' where ''+@ID  
                   if @Sort=0  
                     set @strTmp = @strTmp + ''>(select max(''  
                   else  
                     set @strTmp = @strTmp + '' <(select min(''  
  
               set @strTmp = @strTmp + @ID +'') from (''+ @SqlSelect+'' top ''+ CAST(@pageSize*(@page-1) as Varchar(20)) +'' ''+ @ID +'' from ''+@tblName  
                     +'' where (1=1) '' + @strCondition +'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort+'') AS TBMinID)''  
                     +'' ''+ @strCondition +'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort 
            end             
       end  
       else  
       begin  
         set @page = @pageIndex-@page+1 --后半部分数据处理 
         if @page <= 1 --最后一页数据显示 
                   set @strTmp=@SqlSelect+'' * from (''+@SqlSelect+'' top ''+ CAST(@lastcount as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName  
                     +'' where (1=1) ''+ @strCondition +'' order by ''+ @ID +'' ''+ @strSortType+@fldSort+'') AS TempTB''+'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort                   
         else  
               begin  
                   set @strTmp=@SqlSelect+'' * from (''+@SqlSelect+'' top ''+ CAST(@pageSize as VARCHAR(40))+'' ''+ @fldName+'' from ''+@tblName  
                     +'' where ''+@ID  
                   if @Sort=0  
                     set @strTmp = @strTmp + '' <(select min(''  
                   else  
                     set @strTmp = @strTmp + ''>(select max(''  
            set @strTmp = @strTmp + @ID +'') from(''+ @SqlSelect+'' top ''+ CAST(@pageSize*(@page-2)+@lastcount as Varchar(20)) +'' ''+ @ID +'' from ''+@tblName  
                     +'' where (1=1) ''+ @strCondition +'' order by ''+ @ID +'' ''+ @strSortType+@fldSort+'') AS TBMaxID)''  
                     +'' ''+ @strCondition+'' order by ''+ @ID +'' ''+ @strSortType+@fldSort+'') AS TempTB''+'' order by ''+ @ID+'' ''+ @strFSortType+@fldSort  
               end             
       end    

end  
  
------返回查询结果-----  
SET NOCOUNT off  
exec sp_executesql @strTmp  
' 
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[stock]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[stock](
	[pcode] [varchar](20) NOT NULL,
	[weight] [float] NOT NULL
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[info_product_detail]') AND OBJECTPROPERTY(id, N'IsUserTable') = 1)
BEGIN
CREATE TABLE [dbo].[info_product_detail](
	[pid] [int] NOT NULL,
	[quality] [nvarchar](20) NULL,
	[image] [varchar](100) NULL,
	[pweight] [float] NULL,
	[stand] [nvarchar](20) NULL,
	[premark] [nvarchar](20) NULL
) ON [PRIMARY]
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF NOT EXISTS (SELECT * FROM dbo.sysobjects WHERE id = OBJECT_ID(N'[dbo].[proc_insert_product_notexists]') AND OBJECTPROPERTY(id,N'IsProcedure') = 1)
BEGIN
EXEC dbo.sp_executesql @statement = N'
CREATE PROCEDURE [dbo].[proc_insert_product_notexists]
	@type1 int,
	@type2 int,
	@type3 int,
	@type4 int,
	@pname nvarchar(20),
	@pcode varchar(20),
	@unit nvarchar(10)
AS
BEGIN
	declare @pid int

	declare @sql varchar(1000)

	set @sql = ''insert into info_product(type1,type2,type3,type4,pname,pcode,unit) '' +
			  ''select '' + convert(varchar(20),@type1) + '','' + convert(varchar(20),@type2) + '','' + 
			  convert(varchar(20),@type3) + '','' + convert(varchar(20),@type4) + '','''''' + @pname + '''''','''''' + @pcode + '''''','''''' + @unit + ''''''
			  where not exists(select pcode from info_product where pcode='''''' + @pcode + '''''')''
print(@sql)	
exec(@sql)

	select @pid = @@IDENTITY

	select @pid
END
' 
END
