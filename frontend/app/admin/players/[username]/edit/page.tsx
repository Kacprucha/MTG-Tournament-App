import OuterContainer from '@/app/components/OuterContainer';
import EditPlayerScoreClientPage from './EditPlayerScoreClientPage';

interface EditPlayerScorePageProps {
  params: Promise<{ username: string }>;
}

export default async function EditPlayerScorePage ({ params }: EditPlayerScorePageProps) {
  const awaitedParams = await params;
  const username = awaitedParams.username;

  return (
    <OuterContainer>
      <EditPlayerScoreClientPage username={username} />
    </OuterContainer>
  );
};