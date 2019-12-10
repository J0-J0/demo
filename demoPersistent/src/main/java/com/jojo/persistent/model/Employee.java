package com.jojo.persistent.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.*;

public class Employee implements Serializable {
    @Id
    @Column(name = "Id")
    private Integer id;

    @Column(name = "Name")
    private String name;

    @Column(name = "Salary")
    private BigDecimal salary;

    @Column(name = "ManagerId")
    private Integer managerid;

    private static final long serialVersionUID = 1L;

    public static final String PROP_ID = "id";

    public static final String PROP_NAME = "name";

    public static final String PROP_SALARY = "salary";

    public static final String PROP_MANAGERID = "managerid";

    /**
     * @return Id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return Name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Salary
     */
    public BigDecimal getSalary() {
        return salary;
    }

    /**
     * @param salary
     */
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    /**
     * @return ManagerId
     */
    public Integer getManagerid() {
        return managerid;
    }

    /**
     * @param managerid
     */
    public void setManagerid(Integer managerid) {
        this.managerid = managerid;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", name=").append(name);
        sb.append(", salary=").append(salary);
        sb.append(", managerid=").append(managerid);
        sb.append("]");
        return sb.toString();
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        Employee other = (Employee) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getName() == null ? other.getName() == null : this.getName().equals(other.getName()))
            && (this.getSalary() == null ? other.getSalary() == null : this.getSalary().equals(other.getSalary()))
            && (this.getManagerid() == null ? other.getManagerid() == null : this.getManagerid().equals(other.getManagerid()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getName() == null) ? 0 : getName().hashCode());
        result = prime * result + ((getSalary() == null) ? 0 : getSalary().hashCode());
        result = prime * result + ((getManagerid() == null) ? 0 : getManagerid().hashCode());
        return result;
    }
}