package com.duyp.architecture.mvp.ui.repositories;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.duyp.architecture.mvp.base.presenter.BaseListPresenter;
import com.duyp.architecture.mvp.dagger.qualifier.ActivityContext;
import com.duyp.architecture.mvp.dagger.scopes.PerFragment;
import com.duyp.architecture.mvp.data.local.user.UserManager;
import com.duyp.architecture.mvp.data.model.Repository;
import com.duyp.architecture.mvp.data.repos.RepositoriesRepo;

import java.util.List;

import javax.inject.Inject;

import lombok.Getter;

import static com.duyp.architecture.mvp.data.SimpleNetworkBoundSourceLiveData.TAG;

/**
 * Created by phamd on 9/14/2017.\
 * Presenter for repositories fragment
 */

@PerFragment
public class RepositoryPresenter extends BaseListPresenter<RepositoryView> {

    @NonNull
    @Getter
    private final RepositoryLiveAdapter adapter;

    @Getter
    private final RepositoriesRepo repositoriesRepo;

    private Long sinceRepoId = null;
    private boolean canLoadMore = false;
    private String searchRepoName = "";

    @Inject
    public RepositoryPresenter(@ActivityContext Context context, UserManager userManager,
                               RepositoriesRepo repositoriesRepo, @NonNull RepositoryLiveAdapter adapter) {
        super(context, userManager);
        this.adapter = adapter;
        this.repositoriesRepo = repositoriesRepo;
    }

    private void fetchAllRepositories() {
        addRequest(repositoriesRepo.getAllRepositories(sinceRepoId), repositories -> {
            populateData(repositories);
            canLoadMore = true;
            if (!repositories.isEmpty()) {
                sinceRepoId = repositories.get(repositories.size() - 1).getId();
            }
        });
        updateData();
    }

    void findRepositories(String name) {
        sinceRepoId = null;
        searchRepoName = name;
        if (!name.isEmpty()) {
            canLoadMore = false;
            addRequest(repositoriesRepo.findRepositories(searchRepoName), this::populateData);
            updateData();
        }
    }

    protected void populateData(List<Repository> repositories) {
        Log.d(TAG, "RepositoryPresenter: GOT " + repositories.size() + " items");
        // currently, we don't need to update adapter data since adapter will automatically observe realm data changed
//        adapter.setData(repositories);
        setRefreshed(false);
    }

    protected void updateData() {
        adapter.updateData(repositoriesRepo.getData());
    }

    @Override
    protected void fetchData() {
        if (!searchRepoName.isEmpty()) {
            findRepositories(searchRepoName);
        } else {
            if (isRefreshed()) {
                sinceRepoId = null;
            }
            fetchAllRepositories();
        }
    }

    @Override
    public boolean canLoadMore() {
        return canLoadMore;
    }
}
