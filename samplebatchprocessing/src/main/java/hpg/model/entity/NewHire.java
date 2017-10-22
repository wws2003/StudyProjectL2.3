/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package hpg.model.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created on Oct 16, 2017
 * @author Y@techburg
 */
@Entity
@Table(name = "new_hire", catalog = "hrsample", schema = "glf_schema")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "NewHire.findAll", query = "SELECT n FROM NewHire n"),
    @NamedQuery(name = "NewHire.findByName", query = "SELECT n FROM NewHire n WHERE n.name = :name"),
    @NamedQuery(name = "NewHire.findByHiredate", query = "SELECT n FROM NewHire n WHERE n.hiredate = :hiredate"),
    @NamedQuery(name = "NewHire.findByCdate", query = "SELECT n FROM NewHire n WHERE n.cdate = :cdate"),
    @NamedQuery(name = "NewHire.findByMdate", query = "SELECT n FROM NewHire n WHERE n.mdate = :mdate")})
public class NewHire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "NAME")
    private String name;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "HIREDATE")
    private String hiredate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date cdate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MDATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date mdate;

    public NewHire() {
    }

    public NewHire(String name) {
        this.name = name;
    }

    public NewHire(String name, String hiredate, Date cdate, Date mdate) {
        this.name = name;
        this.hiredate = hiredate;
        this.cdate = cdate;
        this.mdate = mdate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHiredate() {
        return hiredate;
    }

    public void setHiredate(String hiredate) {
        this.hiredate = hiredate;
    }

    public Date getCdate() {
        return cdate;
    }

    public void setCdate(Date cdate) {
        this.cdate = cdate;
    }

    public Date getMdate() {
        return mdate;
    }

    public void setMdate(Date mdate) {
        this.mdate = mdate;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (name != null ? name.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof NewHire)) {
            return false;
        }
        NewHire other = (NewHire) object;
        if ((this.name == null && other.name != null) || (this.name != null && !this.name.equals(other.name))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "hpg.model.entity.NewHire[ name=" + name + " ]";
    }

}
