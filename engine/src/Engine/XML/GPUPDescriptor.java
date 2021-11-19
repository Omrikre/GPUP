//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2021.11.16 at 05:03:46 PM IST 
//


package Engine.XML;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element ref="{}GPUP-Configuration"/>
 *         &lt;element ref="{}GPUP-Targets"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "GPUP-Descriptor")
public class GPUPDescriptor {

    @XmlElement(name = "GPUP-Configuration", required = true)
    protected GPUPConfiguration gpupConfiguration;
    @XmlElement(name = "GPUP-Targets", required = true)
    protected GPUPTargets gpupTargets;

    /**
     * Gets the value of the gpupConfiguration property.
     * 
     * @return
     *     possible object is
     *     {@link GPUPConfiguration }
     *     
     */
    public GPUPConfiguration getGPUPConfiguration() {
        return gpupConfiguration;
    }

    /**
     * Sets the value of the gpupConfiguration property.
     * 
     * @param value
     *     allowed object is
     *     {@link GPUPConfiguration }
     *     
     */
    public void setGPUPConfiguration(GPUPConfiguration value) {
        this.gpupConfiguration = value;
    }

    /**
     * Gets the value of the gpupTargets property.
     * 
     * @return
     *     possible object is
     *     {@link GPUPTargets }
     *     
     */
    public GPUPTargets getGPUPTargets() {
        return gpupTargets;
    }

    /**
     * Sets the value of the gpupTargets property.
     * 
     * @param value
     *     allowed object is
     *     {@link GPUPTargets }
     *     
     */
    public void setGPUPTargets(GPUPTargets value) {
        this.gpupTargets = value;
    }

}