package me.sikozonpc.coursecatalog.entity

import javax.persistence.*

@Entity(name = "Instructors")
data class Instructor(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,

    var name: String,

    @OneToMany(
        fetch = FetchType.LAZY,
        mappedBy = "instructor",
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    val courses: List<Course> = mutableListOf(),
)