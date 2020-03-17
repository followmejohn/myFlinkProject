package com.john.DataStructure;

public class TreeDemo {
    public static void main(String[] args) {
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(1);
        root.right = new TreeNode(3);

        root.left.left = new TreeNode(6);
//        root.left.right = new TreeNode(33);

        root.right.right = new TreeNode(4);
//        root.right.left = new TreeNode(11);

//        root.right.right.left = new TreeNode(7);
//        root.right.right.right = new TreeNode(9);
        System.out.println(balanceTree(root));
    }
    //判断平衡二叉树
    public static  boolean balanceTree(TreeNode root){
        if(root == null) return true;
        if(balanceTree(root.left) && balanceTree(root.right)){
            return Math.abs(height(root.left) - height(root.right)) <= 1;
        }
        return false;
    }
    public static int height(TreeNode root){
        if(root == null) return 0;
        return Math.max(height(root.left), height(root.right)) + 1;
    }
}
class TreeNode{
    private int val;
    TreeNode left;
    TreeNode right;
    TreeNode(int x){ val = x;}
}
