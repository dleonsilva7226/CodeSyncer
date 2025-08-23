import { Tab, Tabs, TabList, TabPanel } from 'react-tabs';
import 'react-tabs/style/react-tabs.css';

export const TabsComponent = ({children}: {children: React.ReactNode[]}) => {
  return (
    <div className="flex flex-col w-full mx-auto mt-8">
      <Tabs className="flex flex-col items-center ">
        <TabList className="flex max-w-xl bg-white rounded-xl shadow mb-4 overflow-hidden w-full">
          <Tab
            className="flex-1 py-3 px-6 text-lg font-semibold text-gray-500 cursor-pointer transition hover:bg-blue-50"
            selectedClassName="bg-blue-600 text-white"
          >
            Refactor
          </Tab>
          <Tab
            className="flex-1 py-3 px-6 text-lg font-semibold text-gray-500 cursor-pointer transition hover:bg-blue-50"
            selectedClassName="bg-blue-600 text-white"
          >
            Merge Suggestions
          </Tab>
        </TabList>
        <TabPanel className="w-full flex justify-center">
          {children[0]}
        </TabPanel>
        <TabPanel className="w-full flex justify-center">
          {children[1]}
        </TabPanel>
      </Tabs>
    </div>
  );
}