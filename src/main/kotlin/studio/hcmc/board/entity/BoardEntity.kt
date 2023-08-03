package studio.hcmc.board.entity

import jakarta.persistence.*
import studio.hcmc.board.domain.BoardDomain
import studio.hcmc.board.dto.BoardDTO
import studio.hcmc.board.vo.BoardVO
import studio.hcmc.kotlin.protocol.DataTransferObjectConsumer
import studio.hcmc.kotlin.protocol.QualifiedValueObjectConvertor
import studio.hcmc.kotlin.protocol.ValueObjectConverter

@Entity
@Table(name = "Board")
class BoardEntity(
    id: Long = 0,
    name: String = ""
) :
    ValueObjectConverter<BoardVO>,
    QualifiedValueObjectConvertor<BoardVO.Qualified>,
    DataTransferObjectConsumer<BoardDTO>,
    BoardDomain<Long>
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false, columnDefinition = "BIGINT")
    override var id = id

    @Column(name = "name", unique = true, nullable = false)
    override var name = name

    override fun toValueObject() = BoardVO(
        id = id,
        name = name
    )

    override fun toQualifiedValueObject() = BoardVO.Qualified(
        id = id,
        name = name
    )

    override fun fromDataTransferObject(dto: BoardDTO) {
        when (dto) {
            is BoardDTO.Post -> fromDataTransferObject(dto)
            is BoardDTO.Put -> fromDataTransferObject(dto)
        }
    }

    private fun fromDataTransferObject(dto: BoardDTO.Post) {
        this.name = dto.name
    }

    private fun fromDataTransferObject(dto: BoardDTO.Put) {
        this.name = dto.name
    }
}
