package ng.com.justjava.corebanking.service.mapper;

import ng.com.justjava.corebanking.domain.Journal;
import ng.com.justjava.corebanking.service.dto.JournalDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {})
public interface JournalMapper extends EntityMapper<JournalDTO, Journal> {


    JournalDTO toDto(Journal journal);

    Journal toEntity(JournalDTO journalDTO);

    default Journal fromId(Long id) {
        if (id == null) {
            return null;
        }
        Journal journal = new Journal();
        journal.setId(id);
        return journal;
    }
}
