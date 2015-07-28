package it.sevenbits.springboottutorial.core.domain;

public class OrderData {

    private Long id_prev;
    private Long id_cur;
    private Long id_next;

    public OrderData(Long id_prev, Long id_cur, Long id_next) {
        this.id_prev = id_prev;
        this.id_cur = id_cur;
        this.id_next = id_next;
    }

    public Long getId_prev() {

        return id_prev;
    }

    public void setId_prev(Long id_prev) {
        this.id_prev = id_prev;
    }

    public Long getId_cur() {
        return id_cur;
    }

    public void setId_cur(Long id_cur) {
        this.id_cur = id_cur;
    }

    public Long getId_next() {
        return id_next;
    }

    public void setId_next(Long id_next) {
        this.id_next = id_next;
    }
}
