/*
 * Copyright 2024-2025 fuhouyu.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import {DataRouter} from "react-router-dom";
import {StateCreator} from "zustand";
import {create} from "zustand/react";
import router from "@/router";


/**
 * routerState
 */
interface RouterState {
  /**
   * router
   */
  router: DataRouter;
}

interface RouterAction {
  /**
   * 存储router
   * @param router router
   */
  storeRouter: (router: DataRouter) => void;
}

/**
 * 创建router slice
 * @param set set
 */
const createRouterSlice: StateCreator<RouterState & RouterAction> = (set) => ({
  router: router,
  storeRouter: (router: DataRouter) => set({router})
});

export const useRouterStore = create<RouterState & RouterAction>((...a) => ({
  ...createRouterSlice(...a)
}));
