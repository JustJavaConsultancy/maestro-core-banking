package ng.com.systemspecs.apigateway.service.mapper;

import ng.com.systemspecs.apigateway.domain.Journal;
import ng.com.systemspecs.apigateway.service.dto.JournalDTO;
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
