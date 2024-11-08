package org.ivipa.ratel.rockie.domain.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.ivipa.ratel.rockie.common.model.Folder;
import org.ivipa.ratel.rockie.common.model.FolderAdd;
import org.ivipa.ratel.rockie.common.model.FolderDelete;
import org.ivipa.ratel.rockie.common.model.FolderPage;
import org.ivipa.ratel.rockie.common.model.FolderQuery;
import org.ivipa.ratel.rockie.common.model.FolderUpdate;
import org.ivipa.ratel.rockie.common.utils.RockieError;
import org.ivipa.ratel.rockie.domain.entity.FolderDo;
import org.ivipa.ratel.rockie.domain.mapper.FolderMapper;
import org.ivipa.ratel.system.common.model.Auth;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class FolderService extends ServiceImpl<FolderMapper, FolderDo> {

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
        if (folderDo == null || folderDo.getDeleted()) {
            throw RockieError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        Folder folder = convertFolderDo(folderDo);
        return folder;
    }

    public Folder getFolder(Auth auth, FolderQuery folderQuery) {
        FolderDo folderDo = getById(folderQuery.getFolderId());
        if (folderDo == null || folderDo.getDeleted()) {
            throw RockieError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        Folder folder = convertFolderDo(folderDo);
        return folder;
    }

    public Folder getFolder(Auth auth, String folderName) {
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_name", folderName)
                .eq("customer_id", auth.getOnlineCustomer().getCustomerId())
                .eq("deleted", 0)
                .isNull("parent_id");
        FolderDo folderDo = this.getOne(queryWrapper);
        if (folderDo != null) {
            Folder folder = convertFolderDo(folderDo);
            return folder;
        } else {
            return null;
        }
    }

    public Folder getFolder(String folderName, Long parentId) {
        QueryWrapper<FolderDo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("folder_name", folderName);
        queryWrapper.eq("parent_id", parentId);
        queryWrapper.eq("deleted", false);
        FolderDo folderDo = this.getOne(queryWrapper);
        if (folderDo != null) {
            Folder folder = convertFolderDo(folderDo);
            return folder;
        } else {
            return null;
        }
    }

    public Folder addFolder(Auth auth, FolderAdd folderAdd) {
        if (folderAdd.getParentId() != null) {
            Folder parentFolder = getFolder(folderAdd.getParentId());
            if (parentFolder == null || !parentFolder.getCustomerId().equals(auth.getOnlineCustomer().getCustomerId())) {
                throw RockieError.FOLDER_PARENT_FOLDER_NOT_FOUND.newException();
            }
            Folder oldFolder = getFolder(folderAdd.getFolderName(), folderAdd.getParentId());
            if (oldFolder != null) {
                throw RockieError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        } else {
            Folder oldFolder = getFolder(auth, folderAdd.getFolderName());
            if (oldFolder != null) {
                throw RockieError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        }
        FolderDo folderDo = convertFolderAdd(folderAdd);
        folderDo.setFolderId(null);
        folderDo.setCustomerId(auth.getOnlineCustomer().getCustomerId());
        folderDo.setCreatedDate(LocalDateTime.now());
        folderDo.setUpdatedDate(LocalDateTime.now());
        save(folderDo);
        Folder folder = convertFolderDo(folderDo);
        return folder;
    }

    public void updateFolder(Auth auth, FolderUpdate folderUpdate) {
        if (folderUpdate.getFolderId() == null) {
            throw RockieError.FOLDER_FOLDER_ID_IS_NULL.newException();
        }
        FolderDo oldFolderDo = getById(folderUpdate.getFolderId());
        if (oldFolderDo == null || oldFolderDo.getDeleted()) {
            throw RockieError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        if (oldFolderDo.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        if (oldFolderDo.getFolderName().equalsIgnoreCase(folderUpdate.getFolderName())) {
            throw RockieError.FOLDER_FOLDER_NAME_EXISTS.newException();
        }
        if(oldFolderDo.getParentId() != null) {
            Folder parentFolder = getFolder(oldFolderDo.getParentId());
            if (parentFolder == null) {
                throw RockieError.FOLDER_PARENT_FOLDER_NOT_FOUND.newException();
            }
            Folder oldFolder = getFolder(folderUpdate.getFolderName(), oldFolderDo.getParentId());
            if (oldFolder != null) {
                throw RockieError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        } else {
            Folder oldFolder = getFolder(auth, folderUpdate.getFolderName());
            if (oldFolder != null) {
                throw RockieError.FOLDER_FOLDER_NAME_EXISTS.newException();
            }
        }
        FolderDo folderDo = convertFolderUpdate(folderUpdate, oldFolderDo);
        folderDo.setUpdatedDate(LocalDateTime.now());
        updateById(folderDo);
    }

    public void deleteFolder(Auth auth, FolderDelete folderDelete) {
        if (folderDelete.getFolderId() == null) {
            throw RockieError.FOLDER_FOLDER_ID_IS_NULL.newException();
        }
        FolderDo oldFolderDo = getById(folderDelete.getFolderId());
        if (oldFolderDo == null || oldFolderDo.getDeleted()) {
            throw RockieError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        if (oldFolderDo.getCustomerId() != auth.getOnlineCustomer().getCustomerId()) {
            throw RockieError.FOLDER_FOLDER_NOT_FOUND.newException();
        }
        FolderDo folderDo = oldFolderDo;
        folderDo.setDeleted(true);
        updateById(folderDo);
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
