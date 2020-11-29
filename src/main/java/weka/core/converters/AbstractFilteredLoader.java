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
 * AbstractFilteredLoader.java
 * Copyright (C) 2020 University of Waikato, Hamilton, New Zealand
 *
 */

package weka.core.converters;

import weka.core.Option;
import weka.core.OptionHandler;
import weka.core.Utils;
import weka.filters.AllFilter;
import weka.filters.Filter;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/**
 * Ancestor for filtered loaders.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @see Loader
 */
public abstract class AbstractFilteredLoader
  extends AbstractFileLoader
  implements OptionHandler {

  /** for serialization */
  private static final long serialVersionUID = 3764533621135196582L;

  /** the base loader. */
  protected AbstractFileLoader m_Loader = new ArffLoader();

  /** the filter to apply. */
  protected Filter m_Filter = new AllFilter();

  /**
   * Get the file extension used for libsvm files
   *
   * @return 		the file extension
   */
  public String getFileExtension() {
    return m_Loader.getFileExtension();
  }

  /**
   * Gets all the file extensions used for this type of file
   *
   * @return the file extensions
   */
  public String[] getFileExtensions() {
    return m_Loader.getFileExtensions();
  }

  /**
   * Returns a description of the file type.
   *
   * @return 		a short file description
   */
  public String getFileDescription() {
    return m_Loader.getFileDescription();
  }

  /**
   * Sets the base loader to use.
   *
   * @param value	the loader
   */
  public void setLoader(AbstractFileLoader value) {
    m_Loader = value;
  }

  /**
   * Returns the base loader in use.
   *
   * @return		the loader
   */
  public AbstractFileLoader getLoader() {
    return m_Loader;
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String loaderTipText() {
    return "The loader to use for loading the data before applying the filter.";
  }

  /**
   * Sets the filter to use.
   *
   * @param value	the filter
   */
  public void setFilter(Filter value) {
    m_Filter = value;
  }

  /**
   * Returns the filter in use.
   *
   * @return		the filter
   */
  public Filter getFilter() {
    return m_Filter;
  }

  /**
   * Returns the tip text for this property
   *
   * @return tip text for this property suitable for displaying in the
   *         explorer/experimenter gui
   */
  public String filterTipText() {
    return "The filter to apply to the loaded data.";
  }

  /**
   * Returns an enumeration describing the available options.
   *
   * @return an enumeration of all the available options.
   */
  public Enumeration listOptions() {
    Vector result = new Vector();

    result.addElement(new Option("\tThe base loader to use\n"
      + "\t(default: " + ArffLoader.class.getName() + ")",
      "L", 1, "-L <classname + options>"));

    result.addElement(new Option("\tThe filter to use\n"
      + "\t(default: " + AllFilter.class.getName() + ")",
      "F", 1, "-F <classname + options>"));

    return result.elements();
  }

  /**
   * Parses a given list of options.
   *
   *
   * @param options the list of options as an array of strings
   * @throws Exception if an option is not supported
   */
  public void setOptions(String[] options) throws Exception {
    String tmpStr;
    String[] optionsTmp;
    String classname;

    tmpStr = Utils.getOption('L', options);
    if (!tmpStr.isEmpty()) {
      optionsTmp = Utils.splitOptions(tmpStr);
      classname = optionsTmp[0];
      optionsTmp[0] = "";
      setLoader((AbstractFileLoader) Utils.forName(AbstractFileLoader.class, classname, optionsTmp));
    }
    else {
      setLoader(new ArffLoader());
    }

    tmpStr = Utils.getOption('F', options);
    if (!tmpStr.isEmpty()) {
      optionsTmp = Utils.splitOptions(tmpStr);
      classname = optionsTmp[0];
      optionsTmp[0] = "";
      setFilter((Filter) Utils.forName(Filter.class, classname, optionsTmp));
    }
    else {
      setFilter(new AllFilter());
    }

    Utils.checkForRemainingOptions(options);
  }

  /**
   * Gets the current settings of the object.
   *
   * @return an array of strings suitable for passing to setOptions
   */
  public String[] getOptions() {
    List<String>	result;

    result = new ArrayList<String>();

    result.add("-L");
    result.add(Utils.toCommandLine(m_Loader));

    result.add("-F");
    result.add(Utils.toCommandLine(m_Filter));

    return result.toArray(new String[0]);
  }
}
