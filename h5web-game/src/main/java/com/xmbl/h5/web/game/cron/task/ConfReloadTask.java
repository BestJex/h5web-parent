package com.xmbl.h5.web.game.cron.task;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.xmbl.h5.web.game.configure.Reloadable;
import com.xmbl.h5.web.game.consts.SystemConst;

import lombok.Data;

@Component
public class ConfReloadTask {
	private List<Reload> reloads = Collections.synchronizedList(new ArrayList<>());

	public void testReload() {
		for (Reload reload : reloads) {
			File file = new File(SystemConst.user_dir + reload.path);
			if (file.exists()) {
				long lastModifyTime = file.lastModified();
				if (lastModifyTime > reload.lastModifyTime) {
					reload.reloadable.reload();
					reload.setLastModifyTime(lastModifyTime);
				}
			}
		}
	}

	@Data
	public static class Reload {
		Reloadable reloadable;
		long lastModifyTime = System.currentTimeMillis();
		String path;

		public Reload(Reloadable reloadable, String path) {
			this.reloadable = reloadable;
			this.path = path;
		}
	}

	public void register(Reloadable reloadable) {
		Reload reload = new Reload(reloadable, reloadable.path());
		reloads.add(reload);
	}
	
	public void init() {
		for (Reload reload : reloads) {
			reload.reloadable.reload();
		}
	}
}
