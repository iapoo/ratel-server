package org.ivipi.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipi.ratel.rockie.common.model.FolderDelete;
import org.ivipi.ratel.rockie.common.model.FolderQuery;
import org.ivipi.ratel.rockie.domain.entity.FolderDo;
import org.ivipi.ratel.rockie.domain.mapper.FolderMapper;
import org.ivipi.ratel.system.common.model.Auth;
import org.ivipi.ratel.rockie.common.model.Folder;
import org.ivipi.ratel.rockie.common.model.FolderAdd;
import org.ivipi.ratel.rockie.common.model.FolderPage;
import org.ivipi.ratel.rockie.common.model.FolderUpdate;
import org.ivipi.ratel.system.common.utils.SystemError;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class FolderService extends ServiceImpl<FolderMapper, FolderDo> {

    public Page<FolderDo> getPage(int pageNum, int pageSize) {
        Page<FolderDo> page = new Page<>(pageNum, pageSize);
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        Page<FolderDo> result = baseMapper.selectPage(page, queryWrapper);
        return result;
    }


    public Page<Folder> getFolders(Auth auth, FolderPage folderPage) {
        Page<Folder> page = new Page<>(folderPage.getPageNum(), folderPage.getPageSize());
        List<Folder> result;
        if(Boolean.TRUE.equals(folderPage.getAllFolders())) {
            result = baseMapper.getAllFolders(page, auth.getOnlineCustomer().getCustomerId());
        } else {
            result = baseMapper.getFolders(page, auth.getOnlineCustomer().getCustomerId(), folderPage.getParentId());
        }
        return page.setRecords(result);
    }

    public Folder getFolder(Long folderId) {
        FolderDo folderDo = getById(folderId);
        Folder folder = convertFolderDo(folderDo);
        return folder;
    }

    public Folder getFolder(Auth auth, FolderQuery folderQuery) {
        FolderDo folderDo = getById(folderQuery.getFolderId());
        Folder folder = convertFolderDo(folderDo);
        return folder;
    }

    public Folder getFolder(Auth auth, String folderName) {
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_name", folderName)
                .eq("customer_id", auth.getOnlineCustomer().getCustomerId())
                .isNull("parent_id");
        FolderDo folderDo = this.getOne(queryWrapper);
        if (folderDo != null) {
            Folder folder = convertFolderDo(folderDo);
            return folder;
        } else {
            return null;
        }
    }

    public Folder getFolder(String folderName) {
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_name", folderName);
        FolderDo folderDo = this.getOne(queryWrapper);
        if (folderDo != null) {
            Folder folder = convertFolderDo(folderDo);
            return folder;
        } else {
            return null;
        }
    }

    public Folder getAvailableFolder(Long folderId) {
        Folder folder = getFolder(folderId);
        if (folder.getEnabled() && !folder.getDeleted()) {
            return folder;
        }
        return null;
    }

    public Folder getFolder(String folderName, Long parentId) {
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_name", folderName);
        queryWrapper.eq("parent_id", parentId);
        FolderDo folderDo = this.getOne(queryWrapper);
        if (folderDo != null) {
            Folder folder = convertFolderDo(folderDo);
            return folder;
        } else {
            return null;
        }
    }

    public void addFolder(Auth auth, FolderAdd folderAdd) {
        if (folderAdd.getParentId() != null) {
            Folder parentFolder = getFolder(folderAdd.getParentId());
            if (parentFolder == null || !parentFolder.getCustomerId().equals(auth.getOnlineCustomer().getCustomerId())) {
                throw SystemError.FOLDER_PARENT_FOLDER_NOT_FOUND.newException();
            }
            Folder oldFolder = getFolder(folderAdd.getFolderName(), folderAdd.getParentId());
            if (oldFolder != null) {
                throw SystemError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        } else {
            Folder oldFolder = getFolder(auth, folderAdd.getFolderName());
            if (oldFolder != null) {
                throw SystemError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        }
        FolderDo folderDo = convertFolderAdd(folderAdd);
        folderDo.setFolderId(null);
        folderDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        save(folderDo);
    }

    public void updateFolder(Auth auth, FolderUpdate folderUpdate) {
        if (folderUpdate.getFolderId() == null) {
            throw SystemError.FOLDER_FOLDER_ID_IS_NULL.newException();
        }
        FolderDo oldFolderDo = getById(folderUpdate.getFolderId());
        if (oldFolderDo == null) {
            throw SystemError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        if (oldFolderDo.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw SystemError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        if (oldFolderDo.getFolderName().equalsIgnoreCase(folderUpdate.getFolderName())) {
            throw SystemError.FOLDER_FOLDER_NAME_EXISTS.newException();
        }
        if(oldFolderDo.getParentId() != null) {
            Folder parentFolder = getFolder(oldFolderDo.getParentId());
            if (parentFolder == null) {
                throw SystemError.FOLDER_PARENT_FOLDER_NOT_FOUND.newException();
            }
            Folder oldFolder = getFolder(folderUpdate.getFolderName(), oldFolderDo.getParentId());
            if (oldFolder != null) {
                throw SystemError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        } else {
            Folder oldFolder = getFolder(auth, folderUpdate.getFolderName());
            if (oldFolder != null) {
                throw SystemError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        }
        FolderDo folderDo = convertFolderUpdate(folderUpdate, oldFolderDo);
        updateById(folderDo);
    }

    public void deleteFolders(Auth auth, FolderDelete folderDelete) {

    }

    private Folder convertFolderDo(FolderDo folderDo) {
        Folder folder = new Folder();
        BeanUtils.copyProperties(folderDo, folder);
        return folder;
    }

    private FolderDo convertFolderAdd(FolderAdd folderAdd) {
        FolderDo folderDo = new FolderDo();
        BeanUtils.copyProperties(folderAdd, folderDo);
        return folderDo;
    }

    private FolderDo convertFolderUpdate(FolderUpdate folderUpdate, FolderDo folderDo) {
        BeanUtils.copyProperties(folderUpdate, folderDo);
        return folderDo;
    }

}
