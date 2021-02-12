package com.zibmbrazil.myFinances.model.service;

import java.util.List;

import com.zibmbrazil.myFinances.model.entity.Release;
import com.zibmbrazil.myFinances.model.enums.Status;

public interface ReleaseService {

	Release save(Release release);

	Release update(Release release);

	void delete(Release release);

	List<Release> search(Release releaseFilter);

	void updateStatus(Release release, Status status);
	
	void validate(Release release);

}
