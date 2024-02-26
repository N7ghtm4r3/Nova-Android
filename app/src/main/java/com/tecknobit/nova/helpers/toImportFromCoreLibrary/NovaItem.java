package com.tecknobit.nova.helpers.toImportFromCoreLibrary;

import com.tecknobit.apimanager.annotations.Structure;

import java.io.Serializable;

@Structure
public abstract class NovaItem implements Serializable {

    protected final String id;

    public NovaItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
