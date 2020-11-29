/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * FilteredIncrementalLoader.java
 * Copyright (C) 2020 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.core.converters;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.RevisionUtils;
import weka.filters.Filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 <!-- globalinfo-start -->
 <!-- globalinfo-end -->
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @see Loader
 */
public class FilteredIncrementalLoader
  extends AbstractFilteredLoader
  implements IncrementalConverter {

  /** for serialization */
  private static final long serialVersionUID = 3764533621135196582L;

  /** the base structure. */
  protected Instances m_BaseStructure;

  /**
   * Returns a string describing this Loader
   * 
   * @return 		a description of the Loader suitable for
   * 			displaying in the explorer/experimenter gui
   */
  public String globalInfo() {
    return "Applies the specified filter to the base loader.\n"
      + "Both, filter and base loader, must be incremental.";
  }

  /**
   * Resets the Loader ready to read a new data set
   * 
   * @throws IOException        if something goes wrong
   */
  public void reset() throws IOException {
    m_structure     = null;
    m_BaseStructure = null;

    setRetrieval(NONE);
    
    if (m_File != null)
      setFile(new File(m_File));

    m_Loader.reset();
  }

  /**
   * Resets the Loader object and sets the source of the data set to be 
   * the supplied File object.
   *
   * @param file 		the source file.
   * @throws IOException        if an error occurs
   */
  public void setSource(File file) throws IOException {
    m_structure     = null;
    m_BaseStructure = null;

    setRetrieval(NONE);

    m_Loader.setSource(file);

    m_sourceFile = file;
    m_File       = file.getAbsolutePath();
  }

  /**
   * Resets the Loader object and sets the source of the data set to be 
   * the supplied InputStream.
   *
   * @param in 			the source InputStream.
   * @throws IOException        if initialization of reader fails.
   */
  public void setSource(InputStream in) throws IOException {
    m_Loader.setSource(in);
    m_File = (new File(System.getProperty("user.dir"))).getAbsolutePath();
  }

  /**
   * Determines and returns (if possible) the structure (internally the 
   * header) of the data set as an empty set of instances.
   *
   * @return 			the structure of the data set as an empty set 
   * 				of Instances
   * @throws IOException        if an error occurs
   */
  public Instances getStructure() throws IOException {
    if (m_structure == null) {
      m_BaseStructure = m_Loader.getStructure();
      try {
	m_Filter.setInputFormat(m_BaseStructure);
	m_structure = m_Filter.getOutputFormat();
      }
      catch (Exception e) {
        throw new IOException("Failed to obtain structure from base loader!", e);
      }
    }

    return new Instances(m_structure, 0);
  }
  
  /**
   * Return the full data set. If the structure hasn't yet been determined
   * by a call to getStructure then method should do so before processing
   * the rest of the data set.
   *
   * @return 			the structure of the data set as an empty 
   * 				set of Instances
   * @throws IOException        if there is no source or parsing fails
   */
  public Instances getDataSet() throws IOException {
    Instances	data;
    Instances	filtered;

    if (getRetrieval() == INCREMENTAL)
      throw new IOException("Cannot mix getting Instances in both incremental and batch modes");

    setRetrieval(BATCH);

    data = m_Loader.getDataSet();
    m_BaseStructure = new Instances(data, 0);
    filtered = null;
    try {
      m_Filter.setInputFormat(data);
      filtered = Filter.useFilter(data, m_Filter);
      m_structure = new Instances(filtered, 0);
    }
    catch (Exception e) {
      m_structure = null;
      throw new IOException("Failed to filter data!", e);
    }

    return filtered;
  }

  /**
   * Reads the next instance.
   *
   * @param structure		ignored
   * @return 			never returns without throwing an exception
   * @throws IOException        always. CommonCSVLoader is unable to process a
   * 				data set incrementally.
   */
  public Instance getNextInstance(Instances structure) throws IOException {
    Instance	inst;
    Instance	filtered;

    inst = m_Loader.getNextInstance(m_BaseStructure);
    // no more data?
    if (inst == null)
      return null;

    try {
      m_Filter.input(inst);
      m_Filter.batchFinished();
      filtered = m_Filter.output();
      return filtered;
    }
    catch (Exception e) {
      throw new IOException("Failed to filter instance!", e);
    }
  }
  
  /**
   * Returns the revision string.
   * 
   * @return		the revision
   */
  public String getRevision() {
    return RevisionUtils.extract("$Revision: 1 $");
  }

  /**
   * Main method.
   *
   * @param args 	should contain the name of an input file.
   */
  public static void main(String[] args) {
    runFileLoader(new FilteredIncrementalLoader(), args);
  }
}
