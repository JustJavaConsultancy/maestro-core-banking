package ng.com.justjava.corebanking.service.mapper;


import ng.com.justjava.corebanking.domain.JournalLine;
import ng.com.justjava.corebanking.domain.Profile;
import ng.com.justjava.corebanking.service.dto.JournalLineDTO;
import ng.com.justjava.corebanking.service.dto.ProfileDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link Profile} and its DTO {@link ProfileDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface JournalLineMapper extends EntityMapper<JournalLineDTO, JournalLine> {

    JournalLineDTO toDto(JournalLine journalLine);


    JournalLine toEntity(JournalLineDTO journalLineDTO);

    default JournalLine fromId(Long id) {
        if (id == null) {
            return null;
        }
        JournalLine journalLine = new JournalLine();
        journalLine.setId(id);
        return journalLine;
    }
}
