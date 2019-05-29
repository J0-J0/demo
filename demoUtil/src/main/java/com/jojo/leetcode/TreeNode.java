package com.jojo.leetcode;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
	int val;
	TreeNode left;
	TreeNode right;

	TreeNode(int x) {
		val = x;
	}

	public static final void widthFirst(TreeNode root) {
		if (root == null) {
			return;
		}
		List<TreeNode> list = new ArrayList<TreeNode>();
		list.add(root);
		for (int i = 0; i < list.size(); i++) {
			TreeNode node = list.get(i);
			System.out.println(node.val);
			if (node.left != null) {
				list.add(node.left);
			}
			if (node.right != null) {
				list.add(node.right);
			}
		}
	}

	/**
	 * 深度遍历优先，同时又分为前序、中序、后序
	 * @param root
	 */
	public static final void depthFirst(TreeNode root) {
		if (root == null) {
			return;
		}
		System.out.println(root.val);
		depthFirst(root.left);
		depthFirst(root.right);
	}

	public static void main(String[] args) {
		/*
		 * 				0
		 * 			/		\
		 * 		1				2
		 * 		/\					\
		 *   3		2					4
		 */
		TreeNode root = new TreeNode(0);

		root.left = new TreeNode(1);
		root.right = new TreeNode(2);
		
		root.left.left = new TreeNode(3);
		root.left.right = new TreeNode(2);
		
		root.right.right = new TreeNode(4);
		
		widthFirst(root);
	}
}
