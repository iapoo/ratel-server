package org.ivipi.ratel.rockie.server.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.ivipi.ratel.common.model.Result;
import org.ivipi.ratel.rockie.common.model.Folder;
import org.ivipi.ratel.rockie.common.model.FolderAdd;
import org.ivipi.ratel.rockie.common.model.FolderDelete;
import org.ivipi.ratel.rockie.common.model.FolderPage;
import org.ivipi.ratel.rockie.common.model.FolderQuery;
import org.ivipi.ratel.rockie.common.model.FolderUpdate;
import org.ivipi.ratel.rockie.domain.service.FolderService;
import org.ivipi.ratel.system.common.annoation.Audit;
import org.ivipi.ratel.system.common.controller.GenericController;
import org.ivipi.ratel.system.common.model.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("folder")
public class FolderController extends GenericController {

    @Autowired
    private FolderService folderService;

    @PostMapping("folders")
    @Audit
    public Result<Page<Folder>> getFolders(Auth auth, @RequestBody FolderPage folderPage) {
        Page<Folder> customers = folderService.getFolders(auth, folderPage);
        return Result.success(customers);
    }

    @PostMapping("folder")
    @Audit
    public Result<Folder> getFolder(Auth auth, @RequestBody FolderQuery folderQuery) {
        Folder folder = folderService.getFolder(auth, folderQuery);
        return Result.success(folder);
    }

    @PostMapping("add")
    @Audit
    public Result<Folder> addFolder(Auth auth, @RequestBody FolderAdd folderAdd) {
        Folder folder = folderService.addFolder(auth, folderAdd);
        return Result.success(folder);
    }

    @PostMapping("update")
    @Audit
    public Result updateFolder(Auth auth, @RequestBody FolderUpdate folderUpdate) {
        folderService.updateFolder(auth, folderUpdate);
        return Result.success();
    }

    @PostMapping("delete")
    @Audit
    public Result deleteFolder(Auth auth, @RequestBody FolderDelete folderDelete) {
        folderService.deleteFolder(auth, folderDelete);
        return Result.success();
    }

}
