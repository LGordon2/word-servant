/**
 * 
 */
package com.app.wordservant;

import java.io.Serializable;

/**
 * @author lewis.gordon
 *
 */
public class Scripture implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1950532942738642984L;
	protected String scriptureReference;
	protected String categoryName;
	protected String scriptureText;
	
	protected Scripture(){
		scriptureReference = new String();
		categoryName = new String();
		scriptureText = new String();
	}
	protected Scripture(String scriptureReference, String categoryName, String scripture){
		this.scriptureText = scripture;
		this.scriptureReference = scriptureReference;
		this.categoryName = categoryName;
	}
	
	public String toString(){
		return scriptureReference;
	}
}
