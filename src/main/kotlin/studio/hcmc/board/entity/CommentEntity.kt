package studio.hcmc.board.entity

import jakarta.persistence.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import studio.hcmc.board.domain.CommentDomain
import studio.hcmc.board.dto.CommentDTO
import studio.hcmc.board.vo.CommentVO
import studio.hcmc.jpa.converter.KotlinInstantConverter
import studio.hcmc.kotlin.protocol.DataTransferObjectConsumer
import studio.hcmc.kotlin.protocol.ValueObjectConverter

@Entity
@Table(name = "Comment")
class CommentEntity(
    id: Long = 0L,
    articleId: Long = 0L,
    body: String = "",
    writerNickname: String = "",
    writerPassword: String = "",
    writerAddress: String = "",
    writtenAt: Instant = Clock.System.now(),
    lastModifiedAt: Instant? = null
) : ValueObjectConverter<CommentVO>,
    DataTransferObjectConsumer<CommentDTO>,
    CommentDomain<Long, Long>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BIGINT")
    override var id = id

    @Column(name = "articleId", nullable = false, updatable = false, columnDefinition = "BIGINT")
    override var articleId = articleId

    @Column(name = "body", nullable = false, length = 200)
    override var body = body

    @Column(name = "writerNickname", nullable = false, length = 20)
    override var writerNickname = writerNickname

    @Column(name = "writerPassword", nullable = false, length = 128)
    override var writerPassword = writerPassword

    @Column(name = "writerAddress", nullable = false, length = 15)
    override var writerAddress = writerAddress

    @Column(name = "writtenAt", nullable = false, columnDefinition = "DATETIME(6)")
    @CreatedDate
    @Convert(converter = KotlinInstantConverter::class)
    override var writtenAt = writtenAt

    @Column(name = "lastModifiedAt", columnDefinition = "DATETIME(6)")
    @LastModifiedDate
    @Convert(converter = KotlinInstantConverter::class)
    override var lastModifiedAt = lastModifiedAt

    override fun toValueObject() = CommentVO(
        id = id,
        articleId = articleId,
        body = body,
        writerNickname = writerNickname,
        writerPassword = writerPassword,
        writerAddress = writerAddress,
        writtenAt = writtenAt,
        lastModifiedAt = lastModifiedAt
    )

    override fun fromDataTransferObject(dto: CommentDTO) {
        when (dto) {
            is CommentDTO.Post -> fromDataTransferObject(dto)
            is CommentDTO.Put -> fromDataTransferObject(dto)
        }
    }

    private fun fromDataTransferObject(dto: CommentDTO.Post) {
        this.body = dto.body
        this.writerNickname = dto.writerNickname
        this.writerPassword = dto.writerPassword
        this.writerAddress = dto.writerAddress
    }

    private fun fromDataTransferObject(dto: CommentDTO.Put) {
        this.body = dto.body
        this.lastModifiedAt = Clock.System.now()
    }
}