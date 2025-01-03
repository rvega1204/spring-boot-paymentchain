/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.paymentchain.exception;
/**
 *
 * @author rvega
 The effort to standardize rest API error reports  is support by ITEF 
 (Internet Engineering Task Force, open standard organization  that which develop and promotes voluntary internet standards) 
 in RFC 7807 which created a generalized error-handling schema composed by five parts.
1- type — A URI identifier that categorizes the error
2-title — A brief, human-readable message about the error
3-code —  The unique error code
4-detail — A human-readable explanation of the error
5-instance — A URI that identifies the specific occurrence of the error
 Standarized is optional but have advantage, it is use for facebook and twitter ie
 https://graph.facebook.com/oauth/access_token?
 * https://api.twitter.com/1.1/statuses/update.json?include_entities=true
 */
public class StandarizedApiExceptionResponse {

    /**
     * URI reference identifying the type of problem.
     */
    private String type = "/errors/uncategorized";

    /**
     * A short, human-readable summary of the problem type.
     */
    private String title;

    /**
     * An application-specific error code.
     */
    private String code;

    /**
     * A detailed human-readable explanation specific to this occurrence of the problem.
     */
    private String detail;

    /**
     * A URI reference that identifies the specific occurrence of the problem.
     */
    private String instance = "/errors/uncategorized/bank";

    /**
     * Constructs a new standardized API exception response.
     *
     * @param title  A short, human-readable summary of the problem type.
     * @param code   An application-specific error code.
     * @param detail A detailed explanation specific to this occurrence of the problem.
     */
    public StandarizedApiExceptionResponse(String title, String code, String detail) {
        super();
        this.title = title;
        this.code = code;
        this.detail = detail;
    }

    /**
     * Gets the error code.
     *
     * @return the error code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the error code.
     *
     * @param code the error code to set.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets the type URI reference.
     *
     * @return the type URI.
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the type URI reference.
     *
     * @param type the type URI to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the title of the error.
     *
     * @return the error title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of the error.
     *
     * @param title the error title to set.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets the detailed explanation of the error.
     *
     * @return the error detail.
     */
    public String getDetail() {
        return detail;
    }

    /**
     * Sets the detailed explanation of the error.
     *
     * @param detail the error detail to set.
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }

    /**
     * Gets the instance URI reference.
     *
     * @return the instance URI.
     */
    public String getInstance() {
        return instance;
    }

    /**
     * Sets the instance URI reference.
     *
     * @param instance the instance URI to set.
     */
    public void setInstance(String instance) {
        this.instance = instance;
    }
}
