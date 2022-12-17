package com.shendawei.recycler.library.adapter.choice;

import com.shendawei.recycler.library.adapter.base.HolderCallback;

/**
 * *
 *
 * @author shendawei
 * @classname ChoiceCallback
 * @date 12/14/22 3:29 AM
 */
public interface ChoiceCallback extends HolderCallback {
    void onChoiceChanged(boolean isAllChecked);
}
