package com.example.onekids_project.entity.tuitionexcel;

import com.example.onekids_project.entity.base.BaseEntity;
import com.example.onekids_project.entity.kids.Kids;
import com.example.onekids_project.entity.user.MaUser;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_kids_excel_t03")
public class OrderKidsExcelT03 extends BaseEntity<String> {
    @Column(name = "order_hidden", columnDefinition = "bit default 0")
    private Boolean orderHidden;

    @Column(name = "kid_code")
    private String kidCode;

    @Column(name = "order_kid_code_t03", length = 45, nullable = false)
    private String orderKidCodeT03;

    @Column(name = "category")
    private String category;

    @Column(name = "arrive_t2t6")
    private Long arriveT2t6;

    @Column(name = "arrive_t7")
    private Long arriveT7;

    @Column(name = "arrive_cn")
    private Long arriveCn;

    @Column(name = "absent_cp_t2t6")
    private Long absentCpT2t6;

    @Column(name = "absent_kp_t2t6")
    private Long absentKpT2t6;

    @Column(name = "absent_cp_t7")
    private Long absentCpT7;

    @Column(name = "absent_kp_t7")
    private Long absentKpT7;

    @Column(name = "leave_later")
    private Long leaveLater;

    @Column(name = "quantity_date")
    private Long quantityDate;

    @Column(name = "before_money")
    private Double beforeMoney;

    @Column(name = "name_kt1")
    private Double nameKt1;
    @Column(name = "money_kt1")
    private Double moneyKt1;
    @Column(name = "quantity_kt1")
    private Double quantityKt1;
    @Column(name = "price_kt1")
    private Double priceKt1;
    @Column(name = "discount_kt1")
    private Double discountKt1;

    @Column(name = "name_kt2")
    private Double nameKt2;
    @Column(name = "money_kt2")
    private Double moneyKt2;
    @Column(name = "quantity_kt2")
    private Double quantityKt2;
    @Column(name = "price_kt2")
    private Double priceKt2;
    @Column(name = "discount_kt2")
    private Double discountKt2;

    @Column(name = "name_kt3")
    private Double nameKt3;
    @Column(name = "money_kt3")
    private Double moneyKt3;
    @Column(name = "quantity_kt3")
    private Double quantityKt3;
    @Column(name = "price_kt3")
    private Double priceKt3;
    @Column(name = "discount_kt3")
    private Double discountKt3;

    @Column(name = "name_kt4")
    private Double nameKt4;
    @Column(name = "money_kt4")
    private Double moneyKt4;
    @Column(name = "quantity_kt4")
    private Double quantityKt4;
    @Column(name = "price_kt4")
    private Double priceKt4;
    @Column(name = "discount_kt4")
    private Double discountKt4;

    @Column(name = "name_kt5")
    private Double nameKt5;
    @Column(name = "money_kt5")
    private Double moneyKt5;
    @Column(name = "quantity_kt5")
    private Double quantityKt5;
    @Column(name = "price_kt5")
    private Double priceKt5;
    @Column(name = "discount_kt5")
    private Double discountKt5;

    @Column(name = "name_kt6")
    private Double nameKt6;
    @Column(name = "money_kt6")
    private Double moneyKt6;
    @Column(name = "quantity_kt6")
    private Double quantityKt6;
    @Column(name = "price_kt6")
    private Double priceKt6;
    @Column(name = "discount_kt6")
    private Double discountKt6;

    @Column(name = "name_kt7")
    private Double nameKt7;
    @Column(name = "money_kt7")
    private Double moneyKt7;
    @Column(name = "quantity_kt7")
    private Double quantityKt7;
    @Column(name = "price_kt7")
    private Double priceKt7;
    @Column(name = "discount_kt7")
    private Double discountKt7;

    @Column(name = "name_kt8")
    private Double nameKt8;
    @Column(name = "money_kt8")
    private Double moneyKt8;
    @Column(name = "quantity_kt8")
    private Double quantityKt8;
    @Column(name = "price_kt8")
    private Double priceKt8;
    @Column(name = "discount_kt8")
    private Double discountKt8;

    @Column(name = "name_kt9")
    private Double nameKt9;
    @Column(name = "money_kt9")
    private Double moneyKt9;
    @Column(name = "quantity_kt9")
    private Double quantityKt9;
    @Column(name = "price_kt9")
    private Double priceKt9;
    @Column(name = "discount_kt9")
    private Double discountKt9;

    @Column(name = "name_kt10")
    private Double nameKt10;
    @Column(name = "money_kt10")
    private Double moneyKt10;
    @Column(name = "quantity_kt10")
    private Double quantityKt10;
    @Column(name = "price_kt10")
    private Double priceKt10;
    @Column(name = "discount_kt10")
    private Double discountKt10;

    @Column(name = "name_kt11")
    private Double nameKt11;
    @Column(name = "money_kt11")
    private Double moneyKt11;
    @Column(name = "quantity_kt11")
    private Double quantityKt11;
    @Column(name = "price_kt11")
    private Double priceKt11;
    @Column(name = "discount_kt11")
    private Double discountKt11;

    @Column(name = "name_kt12")
    private Double nameKt12;
    @Column(name = "money_kt12")
    private Double moneyKt12;
    @Column(name = "quantity_kt12")
    private Double quantityKt12;
    @Column(name = "price_kt12")
    private Double priceKt12;
    @Column(name = "discount_kt12")
    private Double discountKt12;

    @Column(name = "name_kt13")
    private Double nameKt13;
    @Column(name = "money_kt13")
    private Double moneyKt13;
    @Column(name = "quantity_kt13")
    private Double quantityKt13;
    @Column(name = "price_kt13")
    private Double priceKt13;
    @Column(name = "discount_kt13")
    private Double discountKt13;

    @Column(name = "name_kt14")
    private Double nameKt14;
    @Column(name = "money_kt14")
    private Double moneyKt14;
    @Column(name = "quantity_kt14")
    private Double quantityKt14;
    @Column(name = "price_kt14")
    private Double priceKt14;
    @Column(name = "discount_kt14")
    private Double discountKt14;

    @Column(name = "name_kt15")
    private Double nameKt15;
    @Column(name = "money_kt15")
    private Double moneyKt15;
    @Column(name = "quantity_kt15")
    private Double quantityKt15;
    @Column(name = "price_kt15")
    private Double priceKt15;
    @Column(name = "discount_kt15")
    private Double discountKt15;

    @Column(name = "name_kt16")
    private Double nameKt16;
    @Column(name = "money_kt16")
    private Double moneyKt16;
    @Column(name = "quantity_kt16")
    private Double quantityKt16;
    @Column(name = "price_kt16")
    private Double priceKt16;
    @Column(name = "discount_kt16")
    private Double discountKt16;

    @Column(name = "name_kt17")
    private Double nameKt17;
    @Column(name = "money_kt17")
    private Double moneyKt17;
    @Column(name = "quantity_kt17")
    private Double quantityKt17;
    @Column(name = "price_kt17")
    private Double priceKt17;
    @Column(name = "discount_kt17")
    private Double discountKt17;

    @Column(name = "name_kt18")
    private Double nameKt18;
    @Column(name = "money_kt18")
    private Double moneyKt18;
    @Column(name = "quantity_kt18")
    private Double quantityKt18;
    @Column(name = "price_kt18")
    private Double priceKt18;
    @Column(name = "discount_kt18")
    private Double discountKt18;

    @Column(name = "name_kt19")
    private Double nameKt19;
    @Column(name = "money_kt19")
    private Double moneyKt19;
    @Column(name = "quantity_kt19")
    private Double quantityKt19;
    @Column(name = "price_kt19")
    private Double priceKt19;
    @Column(name = "discount_kt19")
    private Double discountKt19;

    @Column(name = "name_kt20")
    private Double nameKt20;
    @Column(name = "money_kt20")
    private Double moneyKt20;
    @Column(name = "quantity_kt20")
    private Double quantityKt20;
    @Column(name = "price_kt20")
    private Double priceKt20;
    @Column(name = "discount_kt20")
    private Double discountKt20;

    @Column(name = "sum_money")
    private Double sumMoney;

    @Column(name = "collected_money")
    private Double collectedMoney;

    @Column(name = "cash_money")
    private Double cashMoney;

    @Column(name = "transfer_money")
    private Double transferMoney;

    @Column(name = "after_money")
    private Double afterMoney;

    @Column(name = "status")
    private String status;

    @Column(name = "parent_unread", columnDefinition = "bit default 0")
    private Boolean parentUnread;

    @Column(name = "note_excel1", length = 1000)
    private String noteExcel1;

    @Column(name = "note_excel2", length = 1000)
    private String noteExcel2;

    @Column(name = "note_excel3", length = 1000)
    private String noteExcel3;

    @JsonManagedReference
    @OneToMany(mappedBy = "orderKidsExcelT03", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<HistoryOrderKids> historyOrderKidsList;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_kids", nullable = false)
    private Kids kids;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user", nullable = false)
    private MaUser maUser;

}
