package com.shendawei.recycler.library.sample;

import com.shendawei.recycler.library.adapter.style.AbsStyleAdapter;

/**
 * Adapter示例
 *
 * @author shendawei
 * @classname SampleAdapter
 * @date 12/14/22 12:41 AM
 */
public class SampleAdapter extends AbsStyleAdapter<SampleCallback> {

    public SampleAdapter(SampleCallback hcb) {
        super(hcb);
    }

    @Override
    protected void generateFactories() {

    }
}
