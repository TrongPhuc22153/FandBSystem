import { useState } from "react";
import { Link } from "react-router";
import { HOME_URI } from "../../constants/WebPageURI";

const items = [
  {
    title: "Dashboard",
    href: "/user/dashboard",
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
        title: "Home",
        href: HOME_URI,
        children: [],
        active: false,
      },
      {
        title: "Product",
        href: "/pages/product",
        children: [
          {
            title: "Features",
            href: "/pages/product/features",
            children: [
              {
                title: "Messaging",
                href: "/pages/product/features/messaging",
                children: [],
                active: false,
              },
              {
                title: "Voice & Video Calls",
                href: "/pages/product/features/voice-video-calls",
                children: [],
                active: false,
              },
            ],
            active: false,
          },
          {
            title: "Integrations",
            href: "/pages/product/integrations",
            children: [
              {
                title: "Slack",
                href: "/pages/product/integrations/slack",
                children: [],
                active: false,
              },
              {
                title: "Discord",
                href: "/pages/product/integrations/discord",
                children: [],
                active: false,
              },
              {
                title: "Microsoft Teams",
                href: "/pages/product/integrations/microsoft-teams",
                children: [],
                active: false,
              },
              {
                title: "Zoom",
                href: "/pages/product/integrations/zoom",
                children: [],
                active: true,
              },
            ],
            active: false,
          },
          {
            title: "Security",
            href: "/pages/product/security",
            children: [],
            active: false,
          },
        ],
        active: false,
      },
      {
        title: "Pricing",
        href: "/pages/pricing",
        children: [],
        active: false,
      },
      {
        title: "Blog",
        href: "/pages/blog",
        children: [
          {
            title: "Newsletter",
            href: "/pages/blog/newsletter",
            children: [],
            active: false,
          },
        ],
        active: false,
      },
      {
        title: "Company",
        href: "/pages/company",
        children: [
          {
            title: "About Us",
            href: "/pages/company/about-us",
            children: [],
            active: false,
          },
          {
            title: "Media & Press",
            href: "/pages/company/media-press",
            children: [],
            active: false,
          },
          {
            title: "Careers",
            href: "/pages/company/careers",
            children: [],
            active: false,
          },
        ],
        active: false,
      },
      {
        title: "Contact",
        href: "/pages/contact",
        children: [],
        active: false,
      },
      {
        title: "404",
        href: "/pages/404",
        children: [],
        active: false,
      },
    ],
  },
  {
    title: "Pages",
    href: "/user/pages",
    icon: (
      <svg
        width="24"
        height="24"
        viewBox="0 0 24 24"
        fill="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M3.6 3H20.4C20.7314 3 21 3.26863 21 3.6V20.4C21 20.7314 20.7314 21 20.4 21H3.6C3.26863 21 3 20.7314 3 20.4V3.6C3 3.26863 3.26863 3 3.6 3Z"
          stroke="currentColor"
          strokeWidth="1.5"
        />
        <path d="M9.75 9.75V21" stroke="currentColor" strokeWidth="1.5" />
        <path d="M3 9.75H21" stroke="currentColor" strokeWidth="1.5" />
      </svg>
    ),
    tree_branchs: [],
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
    href: "/user/notifications",
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
    tree_branchs: [],
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
export default function UserSidebar() {
  const [navigationItems, setNavigationItems] = useState(items);
  const [selectedNavigationItem, setSelectedNavigationItem] = useState(0);
  const [searchValue, setSearchValue] = useState("");

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

    const updatedItems = navigationItems.map((item) => {
      if (item.tree_branchs.length > 0) {
        return {
          ...item,
          tree_branchs: activateBranch(
            deactivateAllBranches(item.tree_branchs),
            leafTitle
          ),
        };
      }
      return item;
    });
    setNavigationItems(updatedItems);
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
                onClick={() => setSelectedNavigationItem(index)}
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
                onClick={() => setSelectedNavigationItem(index + 5)}
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
