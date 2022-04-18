/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.wlznsb.iossupersign.util;

/**
 * @author Rex
 */
public class PNGIHDRTrunk extends PNGTrunk {
  public int m_nWidth;
  public int m_nHeight;

  public PNGIHDRTrunk(int nSize, String szName, byte[] nData, byte[] nCRC) {
    super(nSize, szName, nData, nCRC);

    m_nWidth = readInt(nData, 0);
    m_nHeight = readInt(nData, 4);
  }
}
