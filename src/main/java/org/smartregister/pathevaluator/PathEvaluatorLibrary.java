/**
 * 
 */
package org.smartregister.pathevaluator;

import org.smartregister.pathevaluator.dao.ClientDao;
import org.smartregister.pathevaluator.dao.LocationDao;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Samuel Githengi created on 06/15/20
 */
@Getter
@Setter
public class PathEvaluatorLibrary {
	
	private static PathEvaluatorLibrary instance;
	
	private LocationDao locationDao;
	
	private ClientDao clientDao;
	
	private PathEvaluatorLibrary() {
	}
	
	public static void init() {
		instance = new PathEvaluatorLibrary();
	}
	
	/**
	 * Get the library instance
	 * 
	 * @return the instance
	 */
	public static PathEvaluatorLibrary getInstance() {
		return instance;
	}
	
}
