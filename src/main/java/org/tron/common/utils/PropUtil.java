package org.tron.common.utils;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PropUtil {

  public static String readProperty(String file, String key) {
    InputStream is = null;
    FileInputStream fis = null;
    Properties prop = null;
    try {
      prop = new Properties();
      fis = new FileInputStream(file);
      is = new BufferedInputStream(fis);
      prop.load(is);
      String value = new String(prop.getProperty(key, "").getBytes("ISO-8859-1"), "UTF-8");
      return value;
    } catch (Exception e) {
      logger.error("{}", e);
      return "";
    } finally {
      if (prop != null) {
        prop = null;
      }
      //fis
      try {
        if (fis != null) {
          fis.close();
          fis = null;
        }
      } catch (Exception e) {
        logger.warn("{}", e);
      }
      //is
      try {
        if (is != null) {
          is.close();
          is = null;
        }
      } catch (Exception e) {
        logger.error("{}", e);
      }
    }
  }

  public static boolean writeProperty(String file, String key, String value) {
    FileInputStream fis = null;
    Properties properties = new Properties();
    OutputStream out = null;
    BufferedReader bufferedReader = null;
    BufferedWriter bw = null;

    try {
      fis = new FileInputStream(file);
      bufferedReader = new BufferedReader(new InputStreamReader(fis, UTF_8));
      properties.load(bufferedReader);
      out = new FileOutputStream(file);
      bw = new BufferedWriter(new OutputStreamWriter(out, UTF_8));
      properties.setProperty(key, value);
      properties.store(bw, "Generated by the application.  PLEASE DO NOT EDIT! ");
    } catch (Exception e) {
      logger.warn("{}", e);
      return false;
    } finally {
      //fis
      try {
        if (fis != null) {
          fis.close();
        }
        if (bw != null) {
          bw.close();
        }
      } catch (Exception e) {
        logger.warn("{}", e);
      }
      //out
      try {

        if (Objects.nonNull(out)) {
          out.close();
        }

        if (Objects.nonNull(bufferedReader)) {
          bufferedReader.close();
        }

      } catch (IOException e) {
        logger.warn("{}", e);
      }
    }
    return true;
  }

}