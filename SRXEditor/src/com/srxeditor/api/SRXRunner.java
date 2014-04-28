package com.srxeditor.api;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import net.sourceforge.segment.TextIterator;
import net.sourceforge.segment.srx.SrxDocument;
import net.sourceforge.segment.srx.SrxParser;
import net.sourceforge.segment.srx.io.Srx2Parser;
import net.sourceforge.segment.srx.io.SrxAnyParser;
import net.sourceforge.segment.srx.legacy.FastTextIterator;
import net.sourceforge.segment.util.Util;
import org.openide.filesystems.FileObject;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

public class SRXRunner {

    public static final String EOLN = System.getProperty("line.separator");

    private final String OUTPUT_WINDOW_NAME = "SRX Rules";
    public static final String DEFAULT_BEGIN_SEGMENT = "";
    public static final String DEFAULT_END_SEGMENT = EOLN;

    private final String language;
    private final FileObject[] documents;
    private final InputOutput inputOutput;
    private final SrxDocument srxDocument;

    public SRXRunner(String language, FileObject[] documents, FileObject rules) throws IOException {
        this.inputOutput = IOProvider.getDefault().getIO(OUTPUT_WINDOW_NAME, false);
        this.inputOutput.getOut().reset();
        this.language = language;
        this.documents = documents;

        Reader srxRulesReader = Util.getReader(Util.getFileInputStream(rules.getPath()));
        SrxParser srxParser = new SrxAnyParser(new Srx2Parser());

        this.srxDocument = srxParser.parse(srxRulesReader);
        srxRulesReader.close();
    }

    public void run() throws IOException {
        inputOutput.getOut().println("Running SRX rules test...");

        for (FileObject document : documents) {
            runForSingleFile(document);
        }
        closeSRXRunner();
    }

    private void runForSingleFile(FileObject document) throws IOException {
        inputOutput.getOut().println("Running file: " + document.getName());
        inputOutput.getOut().println();
        performSegment(new FastTextIterator(srxDocument, language, document.asText(), new HashMap<String, Object>()));
        inputOutput.getOut().println("Running file done.");
        inputOutput.getOut().println();
    }

    private void closeSRXRunner() throws IOException {
        this.inputOutput.getOut().close();
        this.inputOutput.getErr().close();
    }

    private void performSegment(TextIterator textIterator) throws IOException {

        String beginSegment = DEFAULT_BEGIN_SEGMENT;
        String endSegment = DEFAULT_END_SEGMENT;

        while (textIterator.hasNext()) {
            String segment = textIterator.next();
            this.inputOutput.getOut().print(beginSegment);
            this.inputOutput.getOut().print(segment);
            this.inputOutput.getOut().print(endSegment);
        }
    }
}
