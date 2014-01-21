package util;

import models.CouchDocument;
import org.ektorp.util.DocumentAccessor;

/**
 * Compatibility for Ektorp, so it knows what ids/revisions to use
 */
public class JacksonDocumentAccessor implements DocumentAccessor {
    @Override
    public boolean hasIdMutator() {
        return false;
    }

    @Override
    public String getId(Object object) {
        return couchDoc(object)._id();
    }

    @Override
    public void setId(Object object, String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRevision(Object object) {
        return couchDoc(object)._rev();
    }

    @Override
    public void setRevision(Object object, String string) {

    }

    private CouchDocument couchDoc(Object obj) {
        if (obj.getClass().isAssignableFrom(CouchDocument.class)) {
            throw new IllegalStateException("Expected class of type "+CouchDocument.class.getName()+" got "+obj.getClass().getName());
        }
        return (CouchDocument)obj;
    }
}
