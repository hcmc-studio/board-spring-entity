package studio.hcmc.board.entity

import jakarta.persistence.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import studio.hcmc.board.domain.ArticleDomain
import studio.hcmc.board.dto.ArticleDTO
import studio.hcmc.board.dto.CommentDTO
import studio.hcmc.board.vo.ArticleVO
import studio.hcmc.jpa.converter.KotlinInstantConverter
import studio.hcmc.kotlin.protocol.DataTransferObjectConsumer
import studio.hcmc.kotlin.protocol.QualifiedValueObjectConvertor
import studio.hcmc.kotlin.protocol.ValueObjectConverter

@Entity
@Table(name = "Article")
class ArticleEntity(
    id: Long = 0,
    boardId: Long = 0,
    title: String = "",
    body: String = "",
    writerNickname: String = "",
    writerPassword: String = "",
    writerAddress: String = "",
    writtenAt: Instant = Instant.fromEpochMilliseconds(0),
    lastModifiedAt: Instant? = null
) :
    ValueObjectConverter<ArticleVO>,
    QualifiedValueObjectConvertor<ArticleVO.Qualified>,
    DataTransferObjectConsumer<ArticleDTO>,
    ArticleDomain<Long, Long>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BIGINT")
    override var id = id

    @Column(name = "boardId", nullable = false, updatable = false, columnDefinition = "BIGINT")
    override var boardId = boardId

    @Column(name = "title", nullable = false, length = 200)
    override var title = title

    @Column(name = "body", nullable = false, length = 2000)
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

    @MapsId("boardId")
    @JoinColumn(name = "boardId", insertable = false, updatable = false)
    @ManyToOne(targetEntity = BoardEntity::class)
    lateinit var board: BoardEntity

    override fun toValueObject() = ArticleVO(
        id = id,
        boardId = boardId,
        title = title,
        body = body,
        writerNickname = writerNickname,
        writerPassword = writerPassword,
        writerAddress = writerAddress,
        writtenAt = writtenAt,
        lastModifiedAt = lastModifiedAt
    )

    override fun toQualifiedValueObject() = ArticleVO.Qualified(
        id = id,
        boardId = boardId,
        board = board.toQualifiedValueObject(),
        title = title,
        body = body,
        writerNickname = writerNickname,
        writerPassword = writerPassword,
        writerAddress = writerAddress,
        writtenAt = writtenAt,
        lastModifiedAt = lastModifiedAt
    )

    override fun fromDataTransferObject(dto: ArticleDTO) {
        when (dto) {
            is ArticleDTO.Post -> fromDataTransferObject(dto)
            is ArticleDTO.Put -> fromDataTransferObject(dto)
        }
    }

    private fun fromDataTransferObject(dto: ArticleDTO.Post) {
        this.title = dto.title
        this.body = dto.body
        this.writerNickname = dto.writerNickname
        this.writerPassword = dto.writerPassword
    }

    private fun fromDataTransferObject(dto: ArticleDTO.Put) {
        this.title = dto.title
        this.body = dto.body
        this.lastModifiedAt = Clock.System.now()
    }
}