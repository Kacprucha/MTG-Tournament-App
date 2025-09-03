"use client";

import React, { useState, useEffect } from 'react';
import { useTournament } from '@/context/TournamentContext';
import { useSession } from 'next-auth/react';
import axios from 'axios';
import { Table, Button, Spin, Alert, Typography } from 'antd';
import { EditOutlined } from '@ant-design/icons';
import Link from 'next/link';
import OuterContainer from '@/app/components/OuterContainer';
import AntDTheme from '@/app/components/AntDTheme';
import { ColumnsType } from 'antd/es/table';

const { Title } = Typography;

interface Participant {
  keycloakId: string;
  username: string;
}

const PlayersListPage = () => {
  const { tournamentId } = useTournament();
  const { data: session } = useSession();
  const [participants, setParticipants] = useState<Participant[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (tournamentId && session?.accessToken) {
      setLoading(true);
      axios.get(`http://localhost:8080/tournaments/${tournamentId}/participants`, {
        headers: { Authorization: `Bearer ${session.accessToken}` }
      })
      .then(response => {
        setParticipants(response.data);
      })
      .catch(err => console.error(err))
      .finally(() => setLoading(false));
    } else {
      setLoading(false);
    }
  }, [tournamentId, session]);

  const columns: ColumnsType<Participant> = [
    { title: 'Nazwa Gracza', dataIndex: 'username', key: 'username' },
    {
      title: 'Akcje',
      key: 'action',
      render: (_, record: Participant) => (
        <Link href={`/admin/players/${record.username}/edit`} passHref>
          <Button icon={<EditOutlined />}>Edytuj Wyniki</Button>
        </Link>
      ),
    },
  ];

  if (loading) return <Spin />;
  if (!tournamentId) { 
    return (
      <div className="min-h-screen flex items-center justify-center">
        <Alert message="Nie wybrano turnieju." type="info" />
      </div>
    );
  }

  return (
    <OuterContainer>
      <AntDTheme>
        <div style={{ padding: '24px' }}>
          <Title level={2}>Zarządzaj Graczami</Title>
          <Table dataSource={participants} columns={columns} rowKey="keycloakId" />
        </div>
      </AntDTheme>
    </OuterContainer>
  );
};

export default PlayersListPage;