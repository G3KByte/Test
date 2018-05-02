/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jacob.ldap;

import java.util.List;
import java.util.ArrayList;

import com.unboundid.asn1.ASN1OctetString;
import com.unboundid.ldap.sdk.*;
import com.unboundid.ldap.sdk.controls.SimplePagedResultsControl;

/**
 *
 * @author jhoel
 */
public class LDAP {
    private static final String domain = "robertscomm.net";
    private static final String urlString = "ldap://192.168.7.245,ldap://192.168.52.16,ldap://192.168.7.16";
    private static final String baseDn = "ou=Employees,dc=robertscomm,dc=net";
    private static final int pageSize = 10;
    private static final String loginAttribute = "samaccountname";
    private static final String userSearchFilter = "memberof=CN=RCN Users,OU=Testing,OU=Employees,DC=robertscomm,DC=net";
    // LDAP Admin credentials
    private static final String adminPrincipal = "ldapadmin@robertscomm.net";
    private static final String adminPass = "r3AD0n!y";
    
    private String[] urls;
    
    public LDAP() {
        urls = urlString.split(",");
    }
    
    public boolean authenticate(String user, String pass) {
        boolean success = false;
        if(!user.equals("") && user != null && !pass.equals("") && pass != null) {
            LDAPConnection ldapConnection = null;
            String loginDn = getLoginDn(user);

            if (loginDn != null) {
                try {
                    ldapConnection = getUserConnection(loginDn, pass);
                    if (ldapConnection != null) {
                        success = true;
                    }
                }
                catch (Exception e) {
                    System.out.println();
                }
                finally {
                    if (ldapConnection != null) {
                        ldapConnection.close();
                    }
                }
            }
        }
        
        return success;
    }
    
    public String getLoginDn(String user) {
        String loginDn = null;
        
        try {
            Filter userFilter = Filter.create(userSearchFilter);
            Filter loginFilter = getLoginFilter(user);
            Filter filter = Filter.createANDFilter(userFilter, loginFilter);
            
            List<SearchResultEntry> searchResult = search(filter);
            if(!searchResult.isEmpty()) {
                loginDn = searchResult.get(0).getDN();
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        return loginDn;
    }
    
    public List<SearchResultEntry> search(Filter filter) {
        return search(filter, new String[]{});
    }
    
    public List<SearchResultEntry> search(Filter filter, String[] attributes) {
	return search(SearchScope.SUB, filter, attributes);
    }
    
    public List<SearchResultEntry> search(SearchScope searchScope, Filter filter, String[] attributes) {
	return search(baseDn, searchScope, filter, attributes);
    }
    
    public List<SearchResultEntry> search(String baseDn, SearchScope searchScope, Filter filter, String[] attributes) {
	return search(baseDn, searchScope, filter, attributes, false);
    }
    
    public List<SearchResultEntry> search(String baseDn, SearchScope searchScope, Filter filter, String[] attributes, boolean paging) {

        List<SearchResultEntry> searchResultEntries = new ArrayList<>();
        
        try {
            LDAPConnection connection = getAdminConnection();

            if (connection != null) { 

                // check if paging should be used
                if (paging) {

                    // create LDAP search request
                    SearchRequest searchRequest = new SearchRequest(baseDn, searchScope, filter, attributes);

                    // instantiate variable for paging cookie
                    ASN1OctetString cookie = null;

                    do {

                        // set controls for LDAP search request
                        Control[] controls = new Control[1];
                        controls[0] = new SimplePagedResultsControl(pageSize, cookie);
                        searchRequest.setControls(controls);

                        // execute LDAP search request
                        SearchResult searchResult = connection.search(searchRequest);

                        // add search entries from page to result list
                        searchResultEntries.addAll(searchResult.getSearchEntries());

                        // get cookie for next page
                        cookie = null;
                        for (Control control : searchResult.getResponseControls()) {
                            if (control instanceof SimplePagedResultsControl) {
                                SimplePagedResultsControl simplePagedResultsControl = (SimplePagedResultsControl) control; 
                                cookie = simplePagedResultsControl.getCookie();
                            }
                        }

                    } 
                    // do this as long as a cookie is returned
                    while ((cookie != null) && (cookie.getValueLength() > 0));
                }
                else {
                    // execute LDAP search request
                    SearchResult searchResult = connection.search(baseDn, searchScope, filter, attributes);

                    // set search entries as result list
                    searchResultEntries = searchResult.getSearchEntries();
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return searchResultEntries;
    }    
    
    private Filter getLoginFilter(String user) {
        return Filter.createEqualityFilter(loginAttribute, user);
    }
    
    private LDAPConnection getAdminConnection() {
        return this.getFailoverLdapConnection(adminPrincipal, adminPass);
    }
    
    private LDAPConnection getUserConnection(String principal, String password) {
        return getFailoverLdapConnection(principal, password);        
    }
    
    private LDAPConnection getFailoverLdapConnection(String principal, String password) {
        for( String url : urls ) {
            LDAPConnection conn = getLdapConnection(principal,password,url.trim());
            if(conn != null) {
                return conn;
            }
        }
        return null;
    }
    
    private LDAPConnection getLdapConnection(String principal, String password, String url) {
        LDAPConnection conn = null;
        
        try {
            LDAPURL ldapUrl = new LDAPURL(url);
            LDAPConnectionOptions connOpt = new LDAPConnectionOptions();
            conn = new LDAPConnection(connOpt, ldapUrl.getHost(), ldapUrl.getPort(), principal, password);
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
        
        return conn;
    }
    
    private LDAPConnection getConnection() throws LDAPException {
        // host, port, username and password
        return new LDAPConnection("robertscomm.net", 389, "jhoel@robertscomm.net", "");
    }
}
