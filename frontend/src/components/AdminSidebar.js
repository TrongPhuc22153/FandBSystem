import { useEffect, useState } from "react";
import { Link } from "react-router";
import {
  ADMIN_CALENDAR_URI,
  ADMIN_CUSTOMERS_URI,
  ADMIN_DASHBOARD_URI,
  ADMIN_NOTIFICATIONS_URI,
  ADMIN_ORDERS_URI,
  ADMIN_PRODUCTS_URI,
  ADMIN_TABLE_CALENDAR_URI,
  ADMIN_TABLES_URI,
  HOME_URI,
} from "../constants/WebPageURI";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faCalendar } from "@fortawesome/free-regular-svg-icons";
import { useLocation } from "react-router-dom";

const items = [
  {
    title: "Dashboard",
    href: ADMIN_DASHBOARD_URI,
    icon: (
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M3 9.5L12 4L21 9.5"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M19 13V19.4C19 19.7314 18.7314 20 18.4 20H5.6C5.26863 20 5 19.7314 5 19.4V13"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    ),
    tree_branchs: [
      {
        title: "Dashboard",
        href: ADMIN_DASHBOARD_URI,
        children: [],
        active: false,
      },
      {
        title: "Customers",
        href: ADMIN_CUSTOMERS_URI,
        children: [],
        active: false,
      },
      {
        title: "Orders",
        href: ADMIN_ORDERS_URI,
        children: [],
        active: false,
      },
      {
        title: "Products",
        href: ADMIN_PRODUCTS_URI,
        children: [],
        active: false,
      },
      {
        title: "Tables",
        href: ADMIN_TABLES_URI,
        children: [],
        active: false,
      },
      {
        title: "Back To Home",
        href: HOME_URI,
        children: [],
        active: false,
      },
    ],
  },
  {
    title: "Calendar",
    href: ADMIN_CALENDAR_URI,
    icon: (
      <FontAwesomeIcon
        icon={faCalendar}
        style={{ height: "24px", width: "24px" }}
      />
    ),
    tree_branchs: [
      {
        title: "Calendar",
        href: ADMIN_CALENDAR_URI,
        children: [],
        active: false,
      },
      {
        title: "Table Calendar",
        href: ADMIN_TABLE_CALENDAR_URI,
        children: [],
        active: false,
      },
      {
        title: "Back To Home",
        href: HOME_URI,
        children: [],
        active: false,
      }
    ],
  },
  {
    title: "Media & Files",
    href: "/user/media-files",
    icon: (
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M2.6954 7.18536L11.6954 11.1854L12.3046 9.81464L3.3046 5.81464L2.6954 7.18536ZM12.75 21.5V10.5H11.25V21.5H12.75ZM12.3046 11.1854L21.3046 7.18536L20.6954 5.81464L11.6954 9.81464L12.3046 11.1854Z"
          fill="currentColor"
        />
        <path
          d="M3 17.1101V6.88992C3 6.65281 3.13964 6.43794 3.35632 6.34164L11.7563 2.6083C11.9115 2.53935 12.0885 2.53935 12.2437 2.6083L20.6437 6.34164C20.8604 6.43794 21 6.65281 21 6.88992V17.1101C21 17.3472 20.8604 17.5621 20.6437 17.6584L12.2437 21.3917C12.0885 21.4606 11.9115 21.4606 11.7563 21.3917L3.35632 17.6584C3.13964 17.5621 3 17.3472 3 17.1101Z"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M7.5 4.5L16.1437 8.34164C16.3604 8.43794 16.5 8.65281 16.5 8.88992V12.5"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    ),
    tree_branchs: [],
  },
  {
    title: "Theme Settings",
    href: "/user/theme-settings",
    icon: (
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M12 2C6.47715 2 2 6.47715 2 12C2 17.5228 6.47715 22 12 22C17.5228 22 22 17.5228 22 12C22 6.47715 17.5228 2 12 2Z"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M8 21.1679V14L12 7L16 14V21.1679"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M8 14C8 14 9.12676 15 10 15C10.8732 15 12 14 12 14C12 14 13.1268 15 14 15C14.8732 15 16 14 16 14"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    ),
    tree_branchs: [],
  },
  {
    title: "Notifications",
    href: ADMIN_NOTIFICATIONS_URI,
    icon: (
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M18 8.4C18 6.70261 17.3679 5.07475 16.2426 3.87452C15.1174 2.67428 13.5913 2 12 2C10.4087 2 8.88258 2.67428 7.75736 3.87452C6.63214 5.07475 6 6.70261 6 8.4C6 15.8667 3 18 3 18H21C21 18 18 15.8667 18 8.4Z"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M13.73 21C13.5542 21.3031 13.3019 21.5547 12.9982 21.7295C12.6946 21.9044 12.3504 21.9965 12 21.9965C11.6496 21.9965 11.3054 21.9044 11.0018 21.7295C10.6982 21.5547 10.4458 21.3031 10.27 21"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    ),
    tree_branchs: [
      {
        title: "Notifications",
        href: ADMIN_NOTIFICATIONS_URI,
        children: [],
        active: false,
      },
      {
        title: "Back To Home",
        href: HOME_URI,
        children: [],
        active: false,
      },
    ],
  },
  {
    title: "Settings",
    href: "/user/settings",
    icon: (
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M12 15C13.6569 15 15 13.6569 15 12C15 10.3431 13.6569 9 12 9C10.3431 9 9 10.3431 9 12C9 13.6569 10.3431 15 12 15Z"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
        <path
          d="M19.6224 10.3954L18.5247 7.7448L20 6L18 4L16.2647 5.48295L13.5578 4.36974L12.9353 2H10.981L10.3491 4.40113L7.70441 5.51596L6 4L4 6L5.45337 7.78885L4.3725 10.4463L2 11V13L4.40111 13.6555L5.51575 16.2997L4 18L6 20L7.79116 18.5403L10.397 19.6123L11 22H13L13.6045 19.6132L16.2551 18.5155C16.6969 18.8313 18 20 18 20L20 18L18.5159 16.2494L19.6139 13.598L21.9999 12.9772L22 11L19.6224 10.3954Z"
          stroke="currentColor"
          strokeWidth="1.5"
          strokeLinecap="round"
          strokeLinejoin="round"
        />
      </svg>
    ),
    tree_branchs: [],
  },
  {
    title: "Your Profile",
    href: "/user/profile",
    icon: (
      <svg
        viewBox="0 0 36 36"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
        width="80"
        height="80"
        colors="#b2cba3,#e0df9f,#e7a83e,#9a736e,#ea525f"
        name="Babe Didrikson"
        size="80"
        className="avatar"
      >
        <mask
          id="mask__beam"
          maskUnits="userSpaceOnUse"
          x="0"
          y="0"
          width="36"
          height="36"
        >
          <rect width="36" height="36" rx="20" fill="white"></rect>
        </mask>
        <g mask="url(#mask__beam)" fill="transparent">
          <rect width="36" height="36" rx="20" fill="#e7a83e"></rect>
          <rect
            x="0"
            y="0"
            width="36"
            height="36"
            transform="translate(-5 9) rotate(249 18 18) scale(1)"
            fill="#ea525f"
            rx="36"
          ></rect>
          <g transform="translate(-1 4.5) rotate(9 18 18)">
            <path d="M13,19 a1,0.75 0 0,0 10,0" fill="black"></path>
            <rect
              x="10"
              y="14"
              width="1.5"
              height="2"
              rx="1"
              stroke="none"
              fill="black"
            ></rect>
            <rect
              x="24"
              y="14"
              width="1.5"
              height="2"
              rx="1"
              stroke="none"
              fill="black"
            ></rect>
          </g>
        </g>
      </svg>
    ),
    tree_branchs: [],
  },
];
export default function AdminSidebar() {
  const [navigationItems, setNavigationItems] = useState(items);
  const [selectedNavigationItem, setSelectedNavigationItem] = useState(0);
  const [searchValue, setSearchValue] = useState("");
  const [activeBranch, setActiveBranch] = useState(null);
  const [activeLeaf, setActiveLeaf] = useState(null);
  const location = useLocation();

  const findItemByHref = (items, href) => {
    for (const item of items) {
      if (item.href === href) return { item, branch: null, leaf: item };
      if (item.tree_branchs) {
        for (const branch of item.tree_branchs) {
          if (branch.href === href) return { item, branch, leaf: branch };
          if (branch.children) {
            for (const child of branch.children) {
              if (child.href === href) return { item, branch, leaf: child };
            }
          }
        }
      }
    }
    return null;
  };

  useEffect(() => {
    // First check localStorage
    const storedBranch = localStorage.getItem("activeBranch");
    const storedLeaf = localStorage.getItem("activeLeaf");
    const storedNavItem = localStorage.getItem("selectedNavigationItem");

    if (storedBranch && storedLeaf && storedNavItem) {
      const branchTitle = JSON.parse(storedBranch).title;
      const updatedItems = items.map((item) => {
        if (item.tree_branchs.length > 0) {
          return {
            ...item,
            tree_branchs: activateBranch(item.tree_branchs, branchTitle),
          };
        }
        return item;
      });

      setNavigationItems(updatedItems);
      setSelectedNavigationItem(parseInt(storedNavItem));
      setActiveBranch(JSON.parse(storedBranch));
      setActiveLeaf(JSON.parse(storedLeaf));
    } else {
      // If no localStorage, match with current URL
      const matched = findItemByHref(items, location.pathname);
      if (matched) {
        const { item, branch, leaf } = matched;
        const itemIndex = items.indexOf(item);

        let updatedItems = [...items];
        if (branch && leaf) {
          updatedItems = updatedItems.map((navItem) => {
            if (navItem.tree_branchs.length > 0) {
              return {
                ...navItem,
                tree_branchs: activateBranch(navItem.tree_branchs, leaf.title),
              };
            }
            return navItem;
          });
        }

        setNavigationItems(updatedItems);
        setSelectedNavigationItem(itemIndex);
        setActiveBranch(branch);
        setActiveLeaf(leaf);

        // Store the initial URL-based selection
        localStorage.setItem("activeBranch", JSON.stringify(branch));
        localStorage.setItem("activeLeaf", JSON.stringify(leaf));
        localStorage.setItem("selectedNavigationItem", itemIndex.toString());
      }
    }
  }, [location.pathname]);

  const activateBranch = (branches, title) => {
    return branches.map((branch) => {
      if (branch.title === title) {
        return { ...branch, active: true };
      }
      if (branch.children.length > 0) {
        return { ...branch, children: activateBranch(branch.children, title) };
      }
      return { ...branch, active: false };
    });
  };

  const handleLeafSelect = (leafTitle) => {
    const deactivateAllBranches = (branches) => {
      return branches.map((branch) => ({
        ...branch,
        active: false,
        children: deactivateAllBranches(branch.children),
      }));
    };

    const activateBranch = (branches, title) => {
      return branches.map((branch) => {
        if (branch.title === title) {
          return { ...branch, active: true };
        }
        if (branch.children.length > 0) {
          return {
            ...branch,
            children: activateBranch(branch.children, title),
          };
        }
        return branch;
      });
    };

    let newActiveBranch = null;
    let newActiveLeaf = null;

    const updatedItems = navigationItems.map((item) => {
      if (item.tree_branchs.length > 0) {
        const updatedBranches = activateBranch(
          deactivateAllBranches(item.tree_branchs),
          leafTitle
        );

        updatedBranches.forEach((branch) => {
          if (branch.active) {
            newActiveBranch = branch;
            newActiveLeaf = branch;
          } else if (branch.children.length > 0) {
            branch.children.forEach((child) => {
              if (child.active) {
                newActiveBranch = branch;
                newActiveLeaf = child;
              }
            });
          }
        });

        return {
          ...item,
          tree_branchs: updatedBranches,
        };
      }
      return item;
    });

    setNavigationItems(updatedItems);
    setActiveBranch(newActiveBranch);
    setActiveLeaf(newActiveLeaf);

    localStorage.setItem("activeBranch", JSON.stringify(newActiveBranch));
    localStorage.setItem("activeLeaf", JSON.stringify(newActiveLeaf));
    localStorage.setItem(
      "selectedNavigationItem",
      selectedNavigationItem.toString()
    );
  };

  const handleNavItemClick = (index) => {
    setSelectedNavigationItem(index);
    localStorage.setItem("selectedNavigationItem", index.toString());
  };

  return (
    <div id="user-navigation" className="d-flex h-100">
      <header className="window-header">
        <nav className="navigation">
          <div className="navigation-top">
            {navigationItems.slice(0, 5).map((item, index) => (
              <Link
                key={index}
                to={item.href}
                className={`navigation-item ${
                  selectedNavigationItem === index ? "active" : ""
                }`}
                onClick={() => handleNavItemClick(index)}
              >
                {item.icon}
                <span className="navigation-item-title">{item.title}</span>
              </Link>
            ))}
          </div>
          <div className="navigation-bottom">
            {navigationItems.slice(5).map((item, index) => (
              <Link
                key={index + 5}
                to={item.href}
                className={`navigation-item ${
                  selectedNavigationItem === index + 5 ? "active" : ""
                }`}
                onClick={() => handleNavItemClick(index + 5)}
              >
                {item.icon}
                <span className="navigation-item-title">{item.title}</span>
              </Link>
            ))}
          </div>
        </nav>
      </header>

      <div className="window-panel flex-grow-1">
        <h2 className="section-title">Site structure</h2>
        <div className="search">
          <input
            type="text"
            className="search-input"
            placeholder="Filter..."
            value={searchValue}
            onChange={(e) => setSearchValue(e.target.value)}
          />
        </div>
        <ul className="tree">
          {navigationItems[selectedNavigationItem].tree_branchs.map(
            (branch, index) => (
              <li key={index} className="tree-branch">
                <div className="tree-branch-action">
                  <Link
                    to={branch.href}
                    className={`tree-branch-link ${
                      branch.active ? "active" : ""
                    }`}
                    onClick={() => handleLeafSelect(branch.title)}
                  >
                    {branch.title}
                  </Link>
                  <button className="tree-branch-button">+</button>
                </div>
                {branch.children.length > 0 && (
                  <ul className="tree tree--sub">
                    {branch.children.map((subBranch, subIndex) => (
                      <li
                        key={subIndex}
                        className="tree-branch tree-branch--sub"
                      >
                        <div className="tree-branch-action">
                          <Link
                            to={subBranch.href}
                            className={`tree-branch-link tree-branch-link--sub ${
                              subBranch.active ? "active" : ""
                            }`}
                            onClick={() => handleLeafSelect(subBranch.title)}
                          >
                            {subBranch.title}
                          </Link>
                          <button className="tree-branch-button">+</button>
                        </div>
                        {subBranch.children.length > 0 && (
                          <ul className="tree tree--sub">
                            {subBranch.children.map(
                              (subSubBranch, subSubIndex) => (
                                <li
                                  key={subSubIndex}
                                  className="tree-branch tree-branch--sub"
                                >
                                  <div className="tree-branch-action">
                                    <Link
                                      to={subSubBranch.href}
                                      className={`tree-branch-link tree-branch-link--sub ${
                                        subSubBranch.active ? "active" : ""
                                      }`}
                                      onClick={() =>
                                        handleLeafSelect(subSubBranch.title)
                                      }
                                    >
                                      {subSubBranch.title}
                                    </Link>
                                    <button className="tree-branch-button">
                                      +
                                    </button>
                                  </div>
                                </li>
                              )
                            )}
                          </ul>
                        )}
                      </li>
                    ))}
                  </ul>
                )}
              </li>
            )
          )}
        </ul>
      </div>
    </div>
  );
}
