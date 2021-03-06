package com.mapbox.mapboxsdk.testapp.camera;

import android.graphics.PointF;

import com.mapbox.mapboxsdk.camera.CameraPosition;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.testapp.activity.BaseActivityTest;
import com.mapbox.mapboxsdk.testapp.activity.espresso.DeviceIndependentTestActivity;
import com.mapbox.mapboxsdk.testapp.utils.TestConstants;

import org.junit.Test;

import static com.mapbox.mapboxsdk.testapp.action.MapboxMapAction.invoke;
import static org.junit.Assert.assertEquals;

public class CameraEaseTest extends BaseActivityTest {

  @Override
  protected Class getActivityClass() {
    return DeviceIndependentTestActivity.class;
  }

  @Test
  public void testEaseToCameraPositionTarget() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      float zoom = 1.0f;
      LatLng moveTarget = new LatLng(1, 1);
      CameraPosition initialPosition = new CameraPosition.Builder().target(
        new LatLng()).zoom(zoom).bearing(0).tilt(0).build();
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Default camera position should match default", cameraPosition, initialPosition);
      mapboxMap.easeCamera(CameraUpdateFactory.newLatLng(moveTarget));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera position latitude should match", cameraPosition.target.getLatitude(),
        moveTarget.getLatitude(), TestConstants.LAT_LNG_DELTA);
      assertEquals("Moved camera position longitude should match", cameraPosition.target.getLongitude(),
        moveTarget.getLongitude(), TestConstants.LAT_LNG_DELTA);
    });
  }

  @Test
  public void testEaseToCameraPositionTargetZoom() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      final float moveZoom = 15.5f;
      final LatLng moveTarget = new LatLng(1.0000000001, 1.0000000003);
      mapboxMap.easeCamera(CameraUpdateFactory.newLatLngZoom(moveTarget, moveZoom));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera position latitude should match", cameraPosition.target.getLatitude(),
        moveTarget.getLatitude(), TestConstants.LAT_LNG_DELTA);
      assertEquals("Moved camera position longitude should match", cameraPosition.target.getLongitude(),
        moveTarget.getLongitude(), TestConstants.LAT_LNG_DELTA);
      assertEquals("Moved zoom should match", cameraPosition.zoom, moveZoom, TestConstants.ZOOM_DELTA);
    });
  }

  @Test
  public void testEaseToCameraPosition() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      final LatLng moveTarget = new LatLng(1.0000000001, 1.0000000003);
      final float moveZoom = 15.5f;
      final float moveTilt = 45.5f;
      final float moveBearing = 12.5f;

      mapboxMap.easeCamera(CameraUpdateFactory.newCameraPosition(
        new CameraPosition.Builder()
          .target(moveTarget)
          .zoom(moveZoom)
          .tilt(moveTilt)
          .bearing(moveBearing)
          .build())
      );
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera position latitude should match", cameraPosition.target.getLatitude(),
        moveTarget.getLatitude(), TestConstants.LAT_LNG_DELTA);
      assertEquals("Moved camera position longitude should match", cameraPosition.target.getLongitude(),
        moveTarget.getLongitude(), TestConstants.LAT_LNG_DELTA);
      assertEquals("Moved zoom should match", cameraPosition.zoom, moveZoom, TestConstants.ZOOM_DELTA);
      assertEquals("Moved zoom should match", cameraPosition.tilt, moveTilt, TestConstants.TILT_DELTA);
      assertEquals("Moved bearing should match", cameraPosition.bearing, moveBearing, TestConstants.BEARING_DELTA);
    });
  }

  @Test
  public void testEaseToBounds() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      final LatLng centerBounds = new LatLng(1, 1);
      LatLng cornerOne = new LatLng();
      LatLng cornerTwo = new LatLng(2, 2);
      final LatLngBounds.Builder builder = new LatLngBounds.Builder();
      builder.include(cornerOne);
      builder.include(cornerTwo);
      mapboxMap.easeCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera position latitude should match center bounds",
        cameraPosition.target.getLatitude(),
        centerBounds.getLatitude(),
        TestConstants.LAT_LNG_DELTA);
      assertEquals("Moved camera position longitude should match center bounds",
        cameraPosition.target.getLongitude(),
        centerBounds.getLongitude(),
        TestConstants.LAT_LNG_DELTA);
    });
  }

  @Test
  public void testEaseToMoveBy() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      final PointF centerPoint = mapboxMap.getProjection().toScreenLocation(mapboxMap.getCameraPosition().target);
      final LatLng moveTarget = new LatLng(2, 2);
      final PointF moveTargetPoint = mapboxMap.getProjection().toScreenLocation(moveTarget);
      mapboxMap.easeCamera(CameraUpdateFactory.scrollBy(
        moveTargetPoint.x - centerPoint.x, moveTargetPoint.y - centerPoint.y));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera position latitude should match", cameraPosition.target.getLatitude(),
        moveTarget.getLatitude(), TestConstants.LAT_LNG_DELTA_LARGE);
      assertEquals("Moved camera position longitude should match", cameraPosition.target.getLongitude(),
        moveTarget.getLongitude(), TestConstants.LAT_LNG_DELTA_LARGE);
    });
  }

  @Test
  public void testEaseToZoomIn() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      float zoom = 1.0f;
      mapboxMap.easeCamera(CameraUpdateFactory.zoomIn());
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera zoom should match moved camera zoom", cameraPosition.zoom, zoom + 1,
        TestConstants.ZOOM_DELTA);
    });
  }

  @Test
  public void testEaseToZoomOut() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      float zoom = 10.0f;
      mapboxMap.easeCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(), zoom));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      mapboxMap.easeCamera(CameraUpdateFactory.zoomOut());
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera zoom should match moved camera zoom", cameraPosition.zoom, zoom - 1,
        TestConstants.ZOOM_DELTA);
    });
  }

  @Test
  public void testEaseToZoomBy() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      float zoom = 1.0f;
      final float zoomBy = 2.45f;
      mapboxMap.easeCamera(CameraUpdateFactory.zoomBy(zoomBy));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera zoom should match moved camera zoom", cameraPosition.zoom, zoom + zoomBy,
        TestConstants.ZOOM_DELTA);
    });
  }

  @Test
  public void testEaseToZoomTo() {
    validateTestSetup();
    invoke(mapboxMap, (uiController, mapboxMap) -> {
      final float zoomTo = 2.45f;
      mapboxMap.easeCamera(CameraUpdateFactory.zoomTo(zoomTo));
      uiController.loopMainThreadForAtLeast(TestConstants.ANIMATION_TEST_TIME);
      CameraPosition cameraPosition = mapboxMap.getCameraPosition();
      assertEquals("Moved camera zoom should match moved camera zoom", cameraPosition.zoom, zoomTo,
        TestConstants.ZOOM_DELTA);
    });
  }
}