package me.sikozonpc.coursecatalog.entity

import javax.persistence.*

@Entity(name = "Courses")
data class Course(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Int?,

    var name: String,

    var category: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    val instructor: Instructor? = null
) {
    override fun toString(): String {
        return "Course(id=$id, name='$name', category='$category', instructor=${instructor?.id})"
    }

}