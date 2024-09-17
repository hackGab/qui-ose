package com.lacouf.rsbjwt.model.auth;

import java.util.HashSet;
import java.util.Set;

public enum Role{
	GESTIONNAIRE("ROLE_GESTIONNAIRE"),
	EMPLOYEUR("ROLE_EMPLOYEUR"),
	ETUDIANT("ROLE_ETUDIANT"),
	;

	private final String string;
	private final Set<Role> managedRoles = new HashSet<>();

	static{
		GESTIONNAIRE.managedRoles.add(EMPLOYEUR);
		GESTIONNAIRE.managedRoles.add(ETUDIANT);
	}

	Role(String string){
		this.string = string;
	}

	@Override
	public String toString(){
		return string;
	}

}
