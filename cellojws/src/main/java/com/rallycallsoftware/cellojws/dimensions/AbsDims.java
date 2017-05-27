/*
 * Created on 2010-07-10
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.rallycallsoftware.cellojws.dimensions;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Objects;

import com.rallycallsoftware.cellojws.general.image.Image;

public class AbsDims implements Dimensions, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 24L;

	public int left = 0;
	public int top = 0;
	public int right = 0;
	public int bottom = 0;

	public AbsDims(final int left_, final int top_, final int right_, final int bottom_) {
		left = left_;
		top = top_;
		right = right_;
		bottom = bottom_;
	}

	public AbsDims(final Rectangle bounds) {
		left = bounds.x;
		top = bounds.y;
		right = bounds.x + bounds.width;
		bottom = bounds.y + bounds.height;
	}

	public AbsDims() {

	}

	@Override
	public AbsDims makeCopy() {
		return new AbsDims(left, top, right, bottom);
	}

	public String toString() {
		return "Left: " + left + " // Top: " + top + " // Right: " + right + " // Bottom: " + bottom;
	}

	@Override
	public int getAbsHeight() {
		return bottom - top;
	}

	@Override
	public int getAbsWidth() {
		return right - left;
	}

	@Override
	public void shrink(final int i) {
		left += i;
		right -= i;
		top += i;
		bottom -= i;
	}

	@Override
	public void move(final int x, final int y) {
		left += x;
		right += x;
		top += y;
		bottom += y;
	}

	@Override
	public Rectangle getRect() {
		final Rectangle rect = new Rectangle();

		rect.height = bottom - top;
		rect.width = right - left;
		rect.x = left;
		rect.y = top;

		return rect;
	}

	@Override
	public AbsDims absoluteify() {
		return this;
	}

	public AbsDims shift(final AbsDims dimensions) {
		final AbsDims ret = new AbsDims();

		ret.left = left + dimensions.left;
		ret.right = right + dimensions.left;
		ret.top = top + dimensions.top;
		ret.bottom = bottom + dimensions.top;

		return ret;
	}

	public AbsDims shiftBack(final AbsDims dimensions) {
		final AbsDims ret = new AbsDims();

		ret.left = left - dimensions.left;
		ret.right = right - dimensions.left;
		ret.top = top - dimensions.top;
		ret.bottom = bottom - dimensions.top;

		return ret;
	}

	public AbsDims getTopHalf() {
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = (bottom - top) / 2 + top;
		ret.right = right;

		return ret;
	}

	public AbsDims getBottomHalf() {
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = (bottom - top) / 2 + top;
		ret.bottom = bottom;
		ret.right = right;

		return ret;

	}

	public AbsDims getLeftHalf() {
		final AbsDims ret = new AbsDims();

		ret.left = left;
		ret.top = top;
		ret.bottom = bottom;
		ret.right = (right - left) / 2 + left;

		return ret;
	}

	public AbsDims getRightHalf() {
		final AbsDims ret = new AbsDims();

		ret.left = (right - left) / 2 + left;
		ret.top = top;
		ret.bottom = bottom;
		ret.right = right;

		return ret;
	}

	public AbsDims center(final Image image, final boolean centerX, final boolean centerY) {
		final AbsDims ret = new AbsDims();

		if (centerX) {
			ret.left = left + (getAbsWidth() - image.getWidth()) / 2;
			ret.right = left + (getAbsWidth() - image.getWidth()) / 2 + image.getWidth();
		} else {
			ret.left = left + 0;
			ret.right = left + image.getWidth();
		}

		if (centerY) {
			ret.top = top + (getAbsHeight() - image.getHeight()) / 2;
			ret.bottom = top + (getAbsHeight() - image.getHeight()) / 2 + image.getHeight();
		} else {
			ret.top = top + 0;
			ret.bottom = top + image.getHeight();
		}

		return ret;
	}

	public static AbsDims center(final Image image, final AbsDims parent) {
		final AbsDims ret = new AbsDims();

		ret.left = (parent.getAbsWidth() - image.getWidth()) / 2;
		ret.right = (parent.getAbsWidth() - image.getWidth()) / 2 + image.getWidth();

		ret.top = (parent.getAbsHeight() - image.getHeight()) / 2;
		ret.bottom = (parent.getAbsHeight() - image.getHeight()) / 2 + image.getHeight();

		return ret;
	}

	/**
	 * Joins this with the target, takes the overall bounding box.
	 * 
	 * @param target
	 */
	public void merge(AbsDims target) {
		if (target == null) {
			return;
		}
		
		left = left < target.left ? left : target.left;
		top = top < target.top ? top : target.top;
		right = right > target.right ? right : target.right;
		bottom = bottom > target.bottom ? bottom : target.bottom;
	}

	/**
	 * Expands the dimensions by the given amount horizontally and vertical,
	 * keeping the same center
	 * 
	 * @param i
	 * @param j
	 */
	public void expand(int i, int j) {
		left -= i;
		top -= j;
		right += i;
		bottom += j;
	}

	public AbsDims takeHorizontalSlice(int i, int length) {
		final AbsDims ret = new AbsDims();

		ret.left = this.left;
		ret.right = this.right;

		final int sliceHeight = getAbsHeight() / length;

		ret.top = this.top + (sliceHeight * i);
		ret.bottom = ret.top + sliceHeight;

		return ret;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (!AbsDims.class.isAssignableFrom(obj.getClass())) {
			return false;
		}

		final AbsDims other = (AbsDims) obj;
		return this.left == other.left && this.right == other.right && this.top == other.top
				&& this.bottom == other.bottom;

	}

	@Override
	public int hashCode() {
		return Objects.hash(left, right, top, bottom);
	}

}
