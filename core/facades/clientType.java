package com.core.facades;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * An enum representing the client types
 * @author Baruch
 * */
@XmlRootElement
public enum clientType {
	ADMIN,COMPANY,CUSTOMER;
}
