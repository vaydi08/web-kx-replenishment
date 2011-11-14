package com.sol.kx.web.service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface ImageService {
	public InputStream load(String img);
	public void save(String picData,String filename) throws IOException;
	public void save(File img,String filename) throws IOException;
}
