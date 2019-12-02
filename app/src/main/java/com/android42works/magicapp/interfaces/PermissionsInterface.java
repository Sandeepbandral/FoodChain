package com.android42works.magicapp.interfaces;

public interface PermissionsInterface {

    void onPermissionsDenied(boolean isPermanentalyDenied);
    void onPermissionsGranted();

}
