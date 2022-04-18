package com.wlznsb.iossupersign.util;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author Rex
 */
public class PNGTrunk {

  protected int m_nSize;
  protected String m_szName;
  protected byte[] m_nData;
  protected byte[] m_nCRC;


  public static PNGTrunk generateTrunk(DataInputStream input) throws IOException {
    int nSize = readPngInt(input);

    byte[] nData = new byte[4];
    input.readFully(nData);
    String szName = new String(nData, "ASCII");

    byte[] nDataBuffer = new byte[nSize];
    input.readFully(nDataBuffer);

    byte[] nCRC = new byte[4];
    input.readFully(nCRC);

    if (szName.equalsIgnoreCase("IHDR")) {
      return new PNGIHDRTrunk(nSize, szName, nDataBuffer, nCRC);
    }

    return new PNGTrunk(nSize, szName, nDataBuffer, nCRC);
  }

  protected PNGTrunk(int nSize, String szName, byte[] nCRC) {
    m_nSize = nSize;
    m_szName = szName;
    m_nCRC = nCRC;
  }

  protected PNGTrunk(int nSize, String szName, byte[] nData, byte[] nCRC) {
    this(nSize, szName, nCRC);
    m_nData = nData;
  }

  public int getSize() {
    return m_nSize;
  }

  public String getName() {
    return m_szName;
  }

  public byte[] getData() {
    return m_nData;
  }

  public byte[] getCRC() {
    return m_nCRC;
  }

  public void writeToStream(FileOutputStream outStream) throws IOException {
    byte nSize[] = new byte[4];
    nSize[0] = (byte) ((m_nSize & 0xFF000000) >> 24);
    nSize[1] = (byte) ((m_nSize & 0xFF0000) >> 16);
    nSize[2] = (byte) ((m_nSize & 0xFF00) >> 8);
    nSize[3] = (byte) (m_nSize & 0xFF);

    outStream.write(nSize);
    outStream.write(m_szName.getBytes("ASCII"));
    outStream.write(m_nData, 0, m_nSize);
    outStream.write(m_nCRC);
  }

  public static void writeInt(byte[] nDes, int nPos, int nVal) {
    nDes[nPos] = (byte) ((nVal & 0xff000000) >> 24);
    nDes[nPos + 1] = (byte) ((nVal & 0xff0000) >> 16);
    nDes[nPos + 2] = (byte) ((nVal & 0xff00) >> 8);
    nDes[nPos + 3] = (byte) (nVal & 0xff);
  }

  public static int readPngInt(DataInputStream input) throws IOException {
    final byte[] buffer = new byte[4];
    input.readFully(buffer);
    return readInt(buffer, 0);
  }

  public static int readInt(byte[] nDest, int nPos) { //读一个int
    return ((nDest[nPos++] & 0xFF) << 24)
      | ((nDest[nPos++] & 0xFF) << 16)
      | ((nDest[nPos++] & 0xFF) << 8)
      | (nDest[nPos] & 0xFF);
  }

  public static void writeCRC(byte[] nData, int nPos) {
    int chunklen = readInt(nData, nPos);
    int sum = CRCChecksum(nData, nPos + 4, 4 + chunklen) ^ 0xffffffff;
    writeInt(nData, nPos + 8 + chunklen, sum);
  }

  public static int[] crc_table = null;

  public static int CRCChecksum(byte[] nBuffer, int nOffset, int nLength) {
    int c = 0xffffffff;
    int n;
    if (crc_table == null) {
      int mkc;
      int mkn, mkk;
      crc_table = new int[256];
      for (mkn = 0; mkn < 256; mkn++) {
        mkc = mkn;
        for (mkk = 0; mkk < 8; mkk++) {
          if ((mkc & 1) == 1) {
            mkc = 0xedb88320 ^ (mkc >>> 1);
          } else {
            mkc = mkc >>> 1;
          }
        }
        crc_table[mkn] = mkc;
      }
    }
    for (n = nOffset; n < nLength + nOffset; n++) {
      c = crc_table[(c ^ nBuffer[n]) & 0xff] ^ (c >>> 8);
    }
    return c;
  }
}
