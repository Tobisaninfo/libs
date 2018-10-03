package de.tobias.utils.application.system;

public enum NativeFeature {
	DOCK_IMAGE("setDockIcon"),
	DOCK_BADGE("setDockIconBadge"),
	DOCK_HIDDEN("setDockIconHidden"),
	APPEARANCE("setAppearance"),
	FILE_IN_FILEVIEW("showFileInFileViewer");

	private String methodName;

	NativeFeature(String methodName) {
		this.methodName = methodName;
	}

	String getMethodName() {
		return methodName;
	}
}
