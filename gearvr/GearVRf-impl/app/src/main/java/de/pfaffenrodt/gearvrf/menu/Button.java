/* Copyright 2015 Samsung Electronics Co., LTD
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.pfaffenrodt.gearvrf.menu;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRMaterial;
import org.gearvrf.GVRMesh;
import org.gearvrf.GVRMeshCollider;
import org.gearvrf.GVRRenderData;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;

import java.util.concurrent.Future;

import de.pfaffenrodt.gearvrf.RenderingOrder;

public abstract class Button extends GVRSceneObject implements Control{
    private final float OPACITY_NOT_SELECTED = 0.5f;
    private final float OPACITY_SELECTED = 1f;

    public Button(GVRContext gvrContext, GVRTexture texture,float width,float height) {
        super(gvrContext);
        init(gvrContext,width,height);
        getRenderData().getMaterial().setMainTexture(texture);
    }

    public Button(GVRContext gvrContext, Future<GVRTexture> texture,float width,float height) {
        super(gvrContext);
        init(gvrContext,width,height);
        getRenderData().getMaterial().setMainTexture(texture);
    }

    protected void init(GVRContext gvrContext,float width,float height) {
        GVRMesh mesh = getGVRContext().createQuad(width, height);

        attachRenderData(new GVRRenderData(gvrContext));
        getRenderData().setMaterial(new GVRMaterial(gvrContext));
        getRenderData().setMesh(mesh);
        getRenderData().setAlphaBlend(true);
        getRenderData().getMaterial().setOpacity(OPACITY_NOT_SELECTED);
        getRenderData().setRenderingOrder(RenderingOrder.MENU);
        attachCollider(new GVRMeshCollider(gvrContext,mesh));
    }

    public void onPick() {
        getRenderData().getMaterial().setOpacity(OPACITY_SELECTED);
    }

    public void onNoPick() {
        getRenderData().getMaterial().setOpacity(OPACITY_NOT_SELECTED);
    }
}
