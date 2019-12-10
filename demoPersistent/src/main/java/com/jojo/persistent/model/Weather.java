package com.jojo.persistent.model;

import java.io.Serializable;
import javax.persistence.*;

public class Weather implements Serializable {
    @Id
    private Integer id;

    private String recorddate;

    private String temperature;

    private static final long serialVersionUID = 1L;

    public static final String PROP_ID = "id";

    public static final String PROP_RECORDDATE = "recorddate";

    public static final String PROP_TEMPERATURE = "temperature";

    /**
     * @return id
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
     * @return recorddate
     */
    public String getRecorddate() {
        return recorddate;
    }

    /**
     * @param recorddate
     */
    public void setRecorddate(String recorddate) {
        this.recorddate = recorddate;
    }

    /**
     * @return temperature
     */
    public String getTemperature() {
        return temperature;
    }

    /**
     * @param temperature
     */
    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", recorddate=").append(recorddate);
        sb.append(", temperature=").append(temperature);
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
        Weather other = (Weather) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getRecorddate() == null ? other.getRecorddate() == null : this.getRecorddate().equals(other.getRecorddate()))
            && (this.getTemperature() == null ? other.getTemperature() == null : this.getTemperature().equals(other.getTemperature()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getRecorddate() == null) ? 0 : getRecorddate().hashCode());
        result = prime * result + ((getTemperature() == null) ? 0 : getTemperature().hashCode());
        return result;
    }
}