/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.paymentchain.customer.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author rvega
 */

/**
 * Represents a standardized API error response following the RFC 7807 specification.
 * This model defines a generalized error-handling schema composed of five key components:
 * <ul>
 *   <li><b>Type:</b> A URI identifier that categorizes the error.</li>
 *   <li><b>Title:</b> A brief, human-readable summary of the error.</li>
 *   <li><b>Code:</b> A unique error code (optional).</li>
 *   <li><b>Detail:</b> A human-readable explanation of the error.</li>
 *   <li><b>Instance:</b> A URI that identifies the specific occurrence of the error.</li>
 * </ul>
 */
@Schema(description = "This model is used to return errors in RFC 7807 format, providing a standardized error schema.")
@NoArgsConstructor
@Data
public class StandarizedApiExceptionResponse {

    /**
     * A URI that identifies the category of the error.
     * Example: "/errors/authentication/not-authorized"
     */
    @Schema(
        description = "The unique URI identifier that categorizes the error.",
        name = "type",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "/errors/authentication/not-authorized"
    )
    private String type;

    /**
     * A brief, human-readable summary of the error.
     * Example: "The user does not have authorization."
     */
    @Schema(
        description = "A brief, human-readable message about the error.",
        name = "title",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "The user does not have authorization."
    )
    private String title;

    /**
     * A unique error code associated with this error (optional).
     * Example: "192"
     */
    @Schema(
        description = "The unique error code.",
        name = "code",
        requiredMode = Schema.RequiredMode.NOT_REQUIRED,
        example = "192"
    )
    private String code;

    /**
     * A detailed, human-readable explanation of the error.
     * Example: "The user does not have the proper permissions to access the resource."
     */
    @Schema(
        description = "A human-readable explanation of the error.",
        name = "detail",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "The user does not have the proper permissions to access the resource. Please contact support at https://sotobotero.com."
    )
    private String detail;

    /**
     * A URI that identifies the specific occurrence of the error.
     * Example: "/errors/authentication/not-authorized/01"
     */
    @Schema(
        description = "A URI that identifies the specific occurrence of the error.",
        name = "instance",
        requiredMode = Schema.RequiredMode.REQUIRED,
        example = "/errors/authentication/not-authorized/01"
    )
    private String instance;

    /**
     * Constructor to create a standardized API error response.
     *
     * @param type   A URI identifier categorizing the error.
     * @param title  A brief summary of the error.
     * @param code   A unique error code (optional).
     * @param detail A detailed explanation of the error.
     */
    public StandarizedApiExceptionResponse (String type, String title, String code, String detail) {
        super();
        this.type = type;
        this.title = title;
        this.code = code;
        this.detail = detail;
    }
}
