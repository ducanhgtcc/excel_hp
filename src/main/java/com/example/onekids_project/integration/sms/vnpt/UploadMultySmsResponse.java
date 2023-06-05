/**
 * UploadMultySmsResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.example.onekids_project.integration.sms.vnpt;

public class UploadMultySmsResponse  implements java.io.Serializable {
    private String description;

    private int errorCode;

    private String listFail;

    private String listSuccess;

    public UploadMultySmsResponse() {
    }

    public UploadMultySmsResponse(
           String description,
           int errorCode,
           String listFail,
           String listSuccess) {
           this.description = description;
           this.errorCode = errorCode;
           this.listFail = listFail;
           this.listSuccess = listSuccess;
    }


    /**
     * Gets the description value for this UploadMultySmsResponse.
     * 
     * @return description
     */
    public String getDescription() {
        return description;
    }


    /**
     * Sets the description value for this UploadMultySmsResponse.
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Gets the errorCode value for this UploadMultySmsResponse.
     * 
     * @return errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }


    /**
     * Sets the errorCode value for this UploadMultySmsResponse.
     * 
     * @param errorCode
     */
    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }


    /**
     * Gets the listFail value for this UploadMultySmsResponse.
     * 
     * @return listFail
     */
    public String getListFail() {
        return listFail;
    }


    /**
     * Sets the listFail value for this UploadMultySmsResponse.
     * 
     * @param listFail
     */
    public void setListFail(String listFail) {
        this.listFail = listFail;
    }


    /**
     * Gets the listSuccess value for this UploadMultySmsResponse.
     * 
     * @return listSuccess
     */
    public String getListSuccess() {
        return listSuccess;
    }


    /**
     * Sets the listSuccess value for this UploadMultySmsResponse.
     * 
     * @param listSuccess
     */
    public void setListSuccess(String listSuccess) {
        this.listSuccess = listSuccess;
    }

    private Object __equalsCalc = null;
    public synchronized boolean equals(Object obj) {
        if (!(obj instanceof UploadMultySmsResponse)) return false;
        UploadMultySmsResponse other = (UploadMultySmsResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.description==null && other.getDescription()==null) || 
             (this.description!=null &&
              this.description.equals(other.getDescription()))) &&
            this.errorCode == other.getErrorCode() &&
            ((this.listFail==null && other.getListFail()==null) || 
             (this.listFail!=null &&
              this.listFail.equals(other.getListFail()))) &&
            ((this.listSuccess==null && other.getListSuccess()==null) || 
             (this.listSuccess!=null &&
              this.listSuccess.equals(other.getListSuccess())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getDescription() != null) {
            _hashCode += getDescription().hashCode();
        }
        _hashCode += getErrorCode();
        if (getListFail() != null) {
            _hashCode += getListFail().hashCode();
        }
        if (getListSuccess() != null) {
            _hashCode += getListSuccess().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(UploadMultySmsResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://sms.mc.vasc.com/", "uploadMultySmsResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("description");
        elemField.setXmlName(new javax.xml.namespace.QName("", "description"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("errorCode");
        elemField.setXmlName(new javax.xml.namespace.QName("", "errorCode"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listFail");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listFail"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("listSuccess");
        elemField.setXmlName(new javax.xml.namespace.QName("", "listSuccess"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           String mechType,
           Class _javaType,
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
