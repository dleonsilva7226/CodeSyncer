"use client";
import { useEffect, useState } from 'react';
import { syncApi } from "../api/syncApi";
import { LoadingPage } from '../components/LoadingPage';
import { FileUploadCard } from '../components/FileUploadCard';
import { TabsComponent } from '../components/Tabs';
import { RefactorCodeForm } from '../components/RefactorCodeForm';
import { MergeCodeForm } from '../components/MergeCodeForm';
import { DiffView } from '../components/DiffView';

const diffText = `
diff --git a/HelloWorld.java b/HelloWorld.java
index e69de29..4b825dc 100644
--- a/HelloWorld.java
+++ b/HelloWorld.java
@@ -1,5 +1,5 @@
 public class HelloWorld {
     public static void main(String[] args) {
-        System.out.println("Hello, Universe!");
+        System.out.println("Hello, World!");
     }
 }
`;

const SynchronizerPage = () => {
  return (
    <div className="flex flex-col items-center justify-center min-h-screen bg-gradient-to-br from-blue-100 via-white to-blue-200 p-4 gap-[40px]">
      <TabsComponent>
          <RefactorCodeForm />
          <MergeCodeForm />
      </TabsComponent>
    </div>
  );
}

export default SynchronizerPage;