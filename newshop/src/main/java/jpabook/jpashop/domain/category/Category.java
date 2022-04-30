package jpabook.jpashop.domain.category;

import javax.persistence.*;
import java.util.List;

@Entity
public class Category {

    @GeneratedValue
    @Id
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> childList;

}
