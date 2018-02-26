package ru.leon4uk.coins.app.domain;

import javax.persistence.*;

@Entity
@Table(name = "currency_pairs")
public class CurrencyPair {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pair_name")
    private String pairName;

    @Column(name = "pair_value")
    private String pairValue;

    @Column(name = "rialto_id")
    private String rialtoId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPairName() {
        return pairName;
    }

    public void setPairName(String pairName) {
        this.pairName = pairName;
    }

    public String getPairValue() {
        return pairValue;
    }

    public void setPairValue(String pairValue) {
        this.pairValue = pairValue;
    }

    public String getRialtoId() {
        return rialtoId;
    }

    public void setRialtoId(String rialtoId) {
        this.rialtoId = rialtoId;
    }
}
