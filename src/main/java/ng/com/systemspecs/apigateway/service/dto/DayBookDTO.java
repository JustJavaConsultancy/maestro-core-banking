package ng.com.systemspecs.apigateway.service.dto;

import ng.com.systemspecs.apigateway.domain.Journal;
import ng.com.systemspecs.apigateway.domain.JournalLine;

import java.util.List;

public class DayBookDTO {
    private Journal journal;
    private List<JournalLine> journalLines;

    public Journal getJournal() {
        return journal;
    }

    public void setJournal(Journal journal) {
        this.journal = journal;
    }

    public List<JournalLine> getJournalLines() {
        return journalLines;
    }

    public void setJournalLines(List<JournalLine> journalLines) {
        this.journalLines = journalLines;
    }


    @Override
    public String toString() {
        return "DayBookDTO{" +
            "journal=" + journal +
            ", journalLines=" + journalLines +
            '}';
    }
}
